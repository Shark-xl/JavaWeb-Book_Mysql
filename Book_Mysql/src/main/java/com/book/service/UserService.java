package com.book.service;

import com.book.entity.User;
import com.book.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // 登录前先做空值检查，再交给数据库查询用户名和密码是否匹配
    public User login(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return null;
        }
        return userMapper.login(username.trim(), password);
    }

    // 注册时先校验非空和用户名唯一性，再插入普通用户 role=0
    public String register(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return "用户名和密码不能为空";
        }
        String cleanUsername = username.trim();
        if (userMapper.findByUsername(cleanUsername) != null) {
            return "用户名已存在";
        }
        User user = new User();
        user.setUsername(cleanUsername);
        user.setPassword(password);
        user.setRole(0);
        userMapper.insert(user);
        return null;
    }
}
