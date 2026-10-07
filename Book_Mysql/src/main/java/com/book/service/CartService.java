package com.book.service;

import com.book.entity.Cart;
import com.book.entity.CartItem;
import com.book.mapper.CartMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {
    private final CartMapper cartMapper;

    public CartService(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    public List<CartItem> findItems(Integer userId) {
        return cartMapper.findItemsByUserId(userId);
    }

    // 添加购物车：同一用户同一商品已存在时只增加数量，不重复插入记录
    public void add(Integer userId, Integer productId) {
        Cart existing = cartMapper.findByUserAndProduct(userId, productId);
        if (existing == null) {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(1);
            cartMapper.insert(cart);
        } else {
            cartMapper.updateQuantity(existing.getId(), existing.getQuantity() + 1);
        }
    }

    public void delete(Integer userId, Integer productId) {
        cartMapper.deleteByUserAndProduct(userId, productId);
    }

    // 计算购物车总价：汇总每个 CartItem 的小计
    public BigDecimal total(List<CartItem> items) {
        return items.stream()
                .map(CartItem::getItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clear(Integer userId) {
        cartMapper.clearByUserId(userId);
    }
}
