package com.book.controller;

import com.book.entity.User;
import com.book.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 登录成功后把 User 放入 Session，后续拦截器和业务代码都通过 currUser 判断当前用户
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        User user = userService.login(username, password);
        if (user == null) {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
        session.setAttribute("currUser", user);
        return user.getRole() != null && user.getRole() == 1 ? "redirect:/admin" : "redirect:/index";
    }

    // 注册普通用户，真正的查重和插入数据库逻辑在 UserService 中完成
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {
        String error = userService.register(username, password);
        if (error != null) {
            model.addAttribute("error", error);
            return "register";
        }
        model.addAttribute("message", "注册成功，请登录");
        return "login";
    }

    // 退出登录时清空 Session
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
