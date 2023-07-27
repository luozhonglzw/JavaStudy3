package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.AuthrizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated//开启验证 邮箱
@RestController//直接RestController 就行了
@RequestMapping("/api/auth")//是与验证相关的 验证邮件
public class AuthorizeController {
    @Resource
    AuthrizeService service;
    private  final String Username_REGEX="^[a-zA-z0-9一-龥]+$";//这里是验证 用户名只能有中文和英文


    /**
     * 这里要导入一个新的spring-boot-starter-validation 就是邮箱要合法
     * @param email
     * 这里写的是HttpSessionId 锁定的是Id 也可以锁定端口Ip地址 校园网的Ip地址都是一样的 如果改为锁定Ip地址的话 1分钟内只允许一个人注册
     * 每一次都是不一样的会话 所以 HttpSessionId 都是不一样的
     * regexp 这里写 验证的正则表达式 验证邮箱是否合理
     */
    //注册邮箱 和重置邮箱 这里做发送邮件的验证
    @PostMapping("/valid-register-email")//需要携带数据 并且需要从前端获得Email数据 @RequestParam("email")可以用这个获得前端表单的email
    public RestBean<String> validateRegisterEmail(@Pattern (regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$")@RequestParam("email") String email,
                                          HttpSession session){
//这里 加一个id 防止别人用用不同的邮件和验证码 session会话有个id 这里注册用户标识为false即没有账户即注册
        String s=service.sendValidateEmail(email,session.getId(),false);
            if(s==null){
                return RestBean.success("邮件已发送 请查收");
            }else {
                return RestBean.failure(400,s);
            }
    }
    //重置邮箱 一定要有账号
    @PostMapping("/valid-reset-email")//需要携带数据 并且需要从前端获得Email数据 @RequestParam("email")可以用这个获得前端表单的email
    public RestBean<String> validateResetEmail(@Pattern (regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$")@RequestParam("email") String email,
                                          HttpSession session){
//这里 加一个id 防止别人用用不同的邮件和验证码 session会话有个id
        String s=service.sendValidateEmail(email,session.getId(),true);
            if(s==null){
                return RestBean.success("邮件已发送 请查收");
            }else {
                return RestBean.failure(400,s);
            }
    }
    @PostMapping("/register")//因为这里需要前端的数据  这里也要验证 否者没有意义 @Pattern(regexp = Username_REGEX)验证的意思 还要判断长度
    public RestBean<String> registerUser(@Pattern(regexp = Username_REGEX) @Length(min=2,max = 8) @RequestParam("username") String username,
                                         @Length(min = 6,max = 16)@RequestParam("password") String password,
                                         @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$")@RequestParam("email") String email,
                                         @Length(min = 6,max = 6)@RequestParam("code")String code,
                                         HttpSession session)//只能是6位
    {
        String s=service.ValidateAndRegister(username, password, email, code,session.getId());
        if(s==null){
            return RestBean.success("注册成功");
        }else {
            return RestBean.failure(400,s);
        }


    }
    /**
     * 这里写重置密码的后端部分
     * 1.发送验证码
     * 2.验证验证码是否正确  正确的话在Session中存一个标记
     * 3.用户发起重置密码请求 如果存在标记的话 就重置
     */
    @PostMapping("/start-rest")//这里是开始重置密码中的 获得验证码
    public RestBean<String> startRest( @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$")@RequestParam("email") String email,
                                       @Length(min = 6,max = 6)@RequestParam("code")String code, HttpSession session){
        String s=service.validateOnly(email,code,session.getId());
        if(s==null){//验证成功向session里丢东西
            session.setAttribute("reset-password",email);//这里建立起全文的session 重置密码的账号
            return RestBean.success();
        }else {
            return RestBean.failure(400,s);
        }
    }
    //这里重置密码 这里要加session.getAttribute("reset-password") 获得唯一的标识 即不能是注册用户中发了验证码 来这里重置密码
    @PostMapping("/do-reset")
    public RestBean<String> resetPassword(@Length(min = 6,max = 16)@RequestParam("password") String password,
                                          HttpSession session){
        String email=(String) session.getAttribute("reset-password");
        if(email==null){
            return RestBean.failure(400,"请先完成邮箱验证");//如果 为空的话 只能是没有验证
        }
        else if(service.resetPassword(password,email)){//改成功
            session.removeAttribute("reset-password");//用完一定要删了
            return RestBean.success("密码重置成功");
        }else {
            return RestBean.failure(400,"出错误了");
        }
    }
}
