package com.example.mapper;

import com.example.entity.auth.Account;
import com.example.entity.user.AccountUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    /**
     *
     * 这里设置 可以通过用户名或者邮箱登录
     */
    @Select("select * from account where username=#{text} or email =#{text}")
    Account findAccountByNameOrEmail(String text);

    @Select("select * from account where username=#{text} or email =#{text}")
    AccountUser findAccountUserByNameOrEmail(String text);

    @Insert("insert into account (email,username,password) values(#{email},#{username},#{password})")
    int createAccount(String username,String password,String email);

    @Update("update account set password=#{password} where email=#{email}")
    int resetPasswordByEmail(String password,String email);
}
