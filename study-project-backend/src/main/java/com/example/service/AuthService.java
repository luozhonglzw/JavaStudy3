package com.example.service;

import com.example.entity.Account;
import com.example.mapper.UserMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {
    @Mapper
    UserMapper userMapper;
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
}
