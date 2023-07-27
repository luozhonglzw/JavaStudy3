package com.example.service;

import com.example.entity.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 这里修改 为所有的接口都写到这个验证接口中
 *
 */

/**
 * 这里写放送邮件
 * 这里下一个是写验证和注册
 */
public interface AuthrizeService extends UserDetailsService {
    String sendValidateEmail(String email,String sessionId);
    String ValidateAndRegister(String username,String password,String email, String code,String sessionId);
}
