package com.example.service.Impl;

import com.example.entity.Account;
import com.example.mapper.UserMapper;
import com.example.service.AuthrizeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 验证接口的实现之一 JavaSpringSecurity
 */
@Service
public class AuthrizeServiceImpl implements AuthrizeService {
    @Value("${spring.mail.username}")//这里是设置发送的邮件人
    String form;
    @Resource
    UserMapper userMapper;

    BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
    @Resource//这里是导入邮箱的模板
    MailSender mailSender;
    @Resource//这里使用redis数据库 如果服务器启动不了 注入失败 把Resource 改为Autowire  这里记得开redis服务器
    StringRedisTemplate template;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username==null){
            throw new UsernameNotFoundException("用户名为空");//这里即没有用户名或者email数据
        }
        Account  account=userMapper.findAccountByNameOrEmail(username);
        System.out.println(userMapper.findAccountByNameOrEmail(username));
        if(account==null){
            throw new UsernameNotFoundException("用户名或密码错误");//这里属于 输入了账号和Email但是数据库中没有
        }
        //这里肯定找到了
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();

    }

    @Override
    public String sendValidateEmail(String email,String sessionId) {

        /*
          1.先成验证码
          2.把邮箱和验证码发送到redis(过期三分钟) 如果此时重新请求发邮件
          剩余时间低于2分钟 即1分钟后才可以重新发
          3.发送验证码到指定邮箱
          4.如果发送失败 把Redis删了
          5.注册时 验证码对比

         */
        String key="email:"+sessionId+":"+email;//冒号分割属性
        //判断
        if(Boolean.TRUE.equals(template.hasKey(key))){//这里要判断是不是有验证码 也就是说验证码在一分钟内不能再发送
            Long expire= Optional.ofNullable(template.getExpire(key,TimeUnit.SECONDS)).orElse(0L);//这里获得验证码剩余生效的时间
            if(expire>120){
                return "请求频繁,请稍后";//如果大于了120返回false 剩余生效的时间 即不能连续点发送在60秒内
            }
        }
        if(userMapper.findAccountByNameOrEmail(email)!=null){
            return "邮箱已经被其他用户注册";
        }
        Random random=new Random();
        int code=random.nextInt(899999)+100000;//一定是6位数
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(form);
        message.setTo(email);
        message.setSubject("您的验证邮件");
        message.setText("验证码: "+code);
        try{
            mailSender.send(message);

            template.opsForValue().set(key,String.valueOf(code),3, TimeUnit.MINUTES);
            return null;
        }catch (MailException e){
               e.printStackTrace();
               return "邮件发送失败 请检查邮箱地址是否有效";
        }
    }

    @Override
    public String ValidateAndRegister(String username, String password, String email, String code,String sessionId) {
        String key="email:"+sessionId+":"+email;//冒号分割属性
        if(Boolean.TRUE.equals(template.hasKey(key))){//先判断数据库中有没有这个key 如果存在继续
            String s=template.opsForValue().get(key);
            if(s==null)return "验证码失效 请重新请求";
            if(s.equals(code)){//请求成功去注册 这里将数据打包进数据库中 即注册
                password=encoder.encode(password);
                if(userMapper.createAccount(username,password,email)>0){
                    return null;
                }
                else {
                    return "内部错误";
                }
            }else {
                return "验证码错误,请检查后提交";
            }
        }
        else {
            return "请先请求一封验证码邮件";
        }
    }
}
