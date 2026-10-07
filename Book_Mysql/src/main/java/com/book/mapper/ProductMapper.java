package com.book.mapper;

import com.book.entity.Product;

import java.util.List;

// 商品表 product 的数据库访问接口
public interface ProductMapper {
    // 查询全部商品
    List<Product> findAll();

    // 根据分类查询商品
    List<Product> findByCategoryId(Integer categoryId);

    // 根据主键查询商品详情
    Product findById(Integer id);

    // 后台新增商品
    int insert(Product product);

    // 后台修改商品
    int update(Product product);

    // 后台删除商品
    int delete(Integer id);

    // 删除商品前检查订单明细是否引用该商品，保护历史订单数据
    int countOrderReferences(Integer id);
}
