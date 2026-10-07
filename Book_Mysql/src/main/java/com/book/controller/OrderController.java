package com.book.controller;

import com.book.entity.User;
import com.book.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 从购物车创建订单：写入 order_main、order_item，并清空 cart
    @PostMapping("/order/create")
    public String create(HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("currUser");
        String error = orderService.create(user.getId());
        if (error != null) {
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/cart";
        }
        redirectAttributes.addFlashAttribute("message", "订单创建成功");
        return "redirect:/orders";
    }

    // 我的订单页：查询订单主表和订单明细
    @GetMapping("/orders")
    public String orders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currUser");
        model.addAttribute("orders", orderService.findUserOrders(user.getId()));
        return "order-list";
    }

    // 确认订单，将订单状态更新为已完成/已支付
    @PostMapping("/orders/{id}/confirm")
    public String confirm(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        orderService.confirm(id);
        redirectAttributes.addFlashAttribute("message", "订单已支付");
        return "redirect:/orders";
    }

    // 取消订单：删除订单主表，数据库级联删除订单明细
    @PostMapping("/orders/{id}/cancel")
    public String cancel(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        orderService.delete(id);
        redirectAttributes.addFlashAttribute("message", "订单已删除");
        return "redirect:/orders";
    }
}
