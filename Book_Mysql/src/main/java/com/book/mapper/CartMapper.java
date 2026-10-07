package com.book.mapper;

import com.book.entity.Cart;
import com.book.entity.CartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 购物车表 cart 的数据库访问接口，SQL 写在 resources/mapper/CartMapper.xml 中
public interface CartMapper {
    // 联表查询某个用户购物车中的商品信息，用于购物车页面展示
    List<CartItem> findItemsByUserId(Integer userId);

    // 根据 user_id 和 product_id 查询购物车记录，用于判断是否已经加入过购物车
    Cart findByUserAndProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);

    // 第一次加入购物车时插入记录
    int insert(Cart cart);

    // 已存在购物车记录时，只更新购买数量
    int updateQuantity(@Param("id") Integer id, @Param("quantity") Integer quantity);

    // 从购物车删除指定商品
    int deleteByUserAndProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);

    // 下单成功后清空当前用户购物车
    int clearByUserId(Integer userId);
}
