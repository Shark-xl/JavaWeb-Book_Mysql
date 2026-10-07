package com.book.entity;

import java.math.BigDecimal;

// 购物车展示对象：由 cart 表和 product 表联表查询得到，不是单独的数据表
public class CartItem {
    private Integer cartId;
    private Integer productId;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private Integer quantity;

    // 小计 = 商品单价 * 数量；金额使用 BigDecimal，避免 double 浮点误差
    public BigDecimal getItemTotal() {
        if (price == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
