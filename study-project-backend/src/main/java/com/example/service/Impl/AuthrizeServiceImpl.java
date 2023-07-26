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
    public boolean sendValidateEmail(String email,String sessionId) {

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
                return false;//如果大于了120返回false 剩余生效的时间 即不能连续点发送在60秒内
            }
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
            return true;
        }catch (MailException e){
               e.printStackTrace();
               return false;
        }
    }
}
