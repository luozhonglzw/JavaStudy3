package com.example.config;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.RestBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    /*
        配置 JavaSecurity
        这里我们只返回json数据 不跳转了

     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                .anyRequest().authenticated()//所有的请求都要验证
                .and()//我们要前端去返回登录界面user
                .formLogin()
                .loginProcessingUrl("/api/auth/login")
                .successHandler(this::onAuthenticationSuccess)//用json格式返回 告诉前端登录是否成功
                .failureHandler(this::onAuthenticationFailure)//这里返回如果又错误的话 （密码错误啥的）也用json格式返回
                .and()
                .logout()
                .logoutUrl("/api/auth/logout")
                .and()
                .csrf()//关闭校验
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(this::onAuthenticationFailure)//这里实现 没登录的话 返回没有权限 而不是重定向
                .and()
                .build();
    }
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setCharacterEncoding("gbk");
        response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功")));
    }//登录成功的处理 默认302重定向controller那 这里以json返回登录的响应 这里要导配置 将字符串转化为json

    /**
     * 如果没登录的话 一般情况302 但是我们想返回没有权限
     * AuthenticationFailureHandler
     */
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setCharacterEncoding("gbk");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401,exception.getMessage())));//401没有权限的意思 自带的报错
    }
}
