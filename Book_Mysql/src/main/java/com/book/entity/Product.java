package com.book.entity;

import java.math.BigDecimal;

// 对应数据库 product 表：商品/图书的基础信息
public class Product {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Integer sales;
    // 外键字段，关联 category.id
    private Integer categoryId;
    // 商品图片访问路径，可以是静态图片路径，也可以是上传后的 /uploads/books/... 路径
    private String imageUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
