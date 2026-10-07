package com.book.config;

import com.book.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 登录成功后用户对象会被放入 Session；没有 currUser 说明未登录，需要跳转登录页
        User user = (User) request.getSession().getAttribute("currUser");
        if (user != null) {
            return true;
        }
        response.sendRedirect("/login");
        return false;
    }
}
