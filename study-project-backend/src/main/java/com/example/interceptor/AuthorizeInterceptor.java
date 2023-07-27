package com.example.interceptor;

import com.example.entity.user.AccountUser;
import com.example.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 这里是写拦截器的
 */
@Component
public class AuthorizeInterceptor implements HandlerInterceptor {
    /**
     * 这个是处理之前
     *  这里获得用户的信息 没有密码
     */
    @Resource
    UserMapper userMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SecurityContext context= SecurityContextHolder.getContext();
        Authentication authentication= context.getAuthentication();//获得已验证的用户
        User user=(User)authentication.getPrincipal();
        String username = user.getUsername();
        AccountUser accountUser=userMapper.findAccountUserByNameOrEmail(username);//这里获得用户的信息
        request.getSession().setAttribute("accountUser",accountUser);//这里把用户的信息往全局里丢 给UserController获得
        return true;
    }
}
