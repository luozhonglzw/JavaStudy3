package com.example.service;

import org.springframework.security.core.userdetails.UserDetailsService;

/*
  这里修改 为所有的接口都写到这个验证接口中

 */

/**
 * 这里写放送邮件
 * 这里下一个是写验证和注册
 */
public interface AuthrizeService extends UserDetailsService {
    //这里需要验证邮件是否已经注册了
    String sendValidateEmail(String email,String sessionId,boolean hasAccount);
    String ValidateAndRegister(String username,String password,String email, String code,String sessionId);
    String validateOnly(String email,String code,String SessionId);
    boolean resetPassword(String password,String email);//这里写的是重置密码
}
