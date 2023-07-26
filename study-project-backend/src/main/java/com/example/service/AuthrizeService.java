package com.example.service;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 这里修改 为所有的接口都写到这个验证接口中
 *
 */

/**
 * 这里写放送邮件
 */
public interface AuthrizeService extends UserDetailsService {
    boolean sendValidateEmail(String email,String sessionId);
}
