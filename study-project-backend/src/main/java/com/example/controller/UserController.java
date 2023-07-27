package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.user.AccountUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/user")
public class UserController {
    /*
      这里写 用户的信息的 就是进入页面后的信息 这里必须登录才能到这里
     */
    @GetMapping("/me")//请求后 拿到信息 这里的SessionAttribute("accountUser")AccountUser user 是全局都有的信息
    public RestBean<AccountUser> me(@SessionAttribute("accountUser")AccountUser user){//这里获得用户的信息后 将信息传个前端
        return RestBean.success(user);
    }
}
