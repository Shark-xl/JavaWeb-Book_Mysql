package com.book.controller;

import com.book.entity.User;
import com.book.service.CategoryService;
import com.book.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    // 首页商品列表：categoryId 为空查全部，不为空按分类查询 product 表
    @GetMapping("/index")
    public String index(@RequestParam(value = "categoryId", required = false) Integer categoryId, Model model) {
        model.addAttribute("products", productService.findByCategoryId(categoryId));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedCategory", categoryService.findById(categoryId));
        return "index";
    }

    // 个人信息页直接展示 Session 中的当前用户
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currUser");
        model.addAttribute("user", user);
        return "profile";
    }
}
