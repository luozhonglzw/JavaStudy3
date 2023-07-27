package com.example.config;

import com.example.interceptor.AuthorizeInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Resource
    AuthorizeInterceptor interceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry
               .addInterceptor(interceptor)//这里登录的通过
               .addPathPatterns("/**");//所有都拦截
    }
}
