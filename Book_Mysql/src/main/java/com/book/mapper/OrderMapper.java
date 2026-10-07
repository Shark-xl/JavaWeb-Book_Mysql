package com.book.mapper;

import com.book.entity.OrderItem;
import com.book.entity.OrderMain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 订单主表 order_main 和订单明细表 order_item 的数据库访问接口
public interface OrderMapper {
    // 插入订单主表，并回填自增主键 id
    int insertOrder(OrderMain order);

    // 插入订单明细表，一条订单可以对应多条明细
    int insertOrderItem(OrderItem item);

    // 查询某个用户的所有订单主表记录
    List<OrderMain> findByUserId(Integer userId);

    // 查询某个订单下的全部明细，并关联商品名称和图片
    List<OrderItem> findItemsByOrderId(Integer orderId);

    // 修改订单状态，例如确认/支付
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    // 删除订单主表；数据库外键会级联删除 order_item 明细
    int delete(Integer id);
}
