package com.book.entity;

// 对应数据库 user 表：保存登录账号、密码和角色
public class User {
    private Integer id;
    private String username;
    private String password;
    // 角色字段：0 表示普通用户，1 表示管理员
    private Integer role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
