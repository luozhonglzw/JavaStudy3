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
    @PostMapping("/valid-email")//需要携带数据 并且需要从前端获得Email数据 @RequestParam("email")可以用这个获得前端表单的email
    public RestBean<String> validateEmail(@Pattern (regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$")@RequestParam("email") String email,
                                          HttpSession session){
//这里 加一个id 防止别人用用不同的邮件和验证码 session会话有个id
        String s=service.sendValidateEmail(email,session.getId());
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
}
