package com.example.entity.user;

import lombok.Data;

/**
 * 这里设置登录成功后 返回的数据
 */

@Data
public class AccountUser {
    int id;
    String username;
    String email;
}
