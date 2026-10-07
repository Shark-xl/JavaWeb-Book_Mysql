package com.book.mapper;

import com.book.entity.User;
import org.apache.ibatis.annotations.Param;

// 用户表 user 的数据库访问接口
public interface UserMapper {
    // 登录校验：根据用户名和密码查询用户
    User login(@Param("username") String username, @Param("password") String password);

    // 注册查重：判断用户名是否已经存在
    User findByUsername(String username);

    // 根据主键查询用户
    User findById(Integer id);

    // 注册新用户
    int insert(User user);
}
