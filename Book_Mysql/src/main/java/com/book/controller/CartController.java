package com.book.controller;

import com.book.entity.CartItem;
import com.book.entity.User;
import com.book.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 购物车页面：联表查询 cart 和 product，并计算总价
    @GetMapping("/cart")
    public String cart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currUser");
        List<CartItem> items = cartService.findItems(user.getId());
        model.addAttribute("items", items);
        model.addAttribute("total", cartService.total(items));
        return "cart";
    }

    // 加入购物车：如果该商品已存在则数量 +1，否则插入 cart 新记录
    @PostMapping("/cart/add/{productId}")
    public String add(@PathVariable Integer productId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("currUser");
        cartService.add(user.getId(), productId);
        redirectAttributes.addFlashAttribute("message", "已加入购物车");
        return "redirect:/index";
    }

    // 删除购物车中的一个商品
    @PostMapping("/cart/delete/{productId}")
    public String delete(@PathVariable Integer productId, HttpSession session) {
        User user = (User) session.getAttribute("currUser");
        cartService.delete(user.getId(), productId);
        return "redirect:/cart";
    }
}
