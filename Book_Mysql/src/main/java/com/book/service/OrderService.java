package com.book.service;

import com.book.entity.CartItem;
import com.book.entity.OrderItem;
import com.book.entity.OrderMain;
import com.book.mapper.CartMapper;
import com.book.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;

    public OrderService(CartMapper cartMapper, OrderMapper orderMapper) {
        this.cartMapper = cartMapper;
        this.orderMapper = orderMapper;
    }

    // 下单涉及订单主表、订单明细、购物车三类数据，必须放在一个事务中保证一致性
    @Transactional
    public String create(Integer userId) {
        List<CartItem> cartItems = cartMapper.findItemsByUserId(userId);
        if (cartItems.isEmpty()) {
            return "购物车为空，无法下单";
        }

        // 根据购物车商品单价和数量计算订单总价
        BigDecimal total = cartItems.stream()
                .map(CartItem::getItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 先插入订单主表 order_main，得到自增 id 后才能插入订单明细
        OrderMain order = new OrderMain();
        //时间
        order.setOrderNumber(UUID.randomUUID().toString().replace("-", "").substring(0, 10));
        order.setUserId(userId);
        order.setTotalPrice(total);
        order.setStatus(0);
        orderMapper.insertOrder(order);

        // 再把购物车中的每个商品复制到 order_item，保存下单时单价
        for (CartItem cartItem : cartItems) {
            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(cartItem.getProductId());
            item.setQuantity(cartItem.getQuantity());
            item.setUnitPrice(cartItem.getPrice());
            orderMapper.insertOrderItem(item);
        }

        // 订单创建成功后清空购物车；如果上面任一步失败，事务会回滚，不会清空
        cartMapper.clearByUserId(userId);
        return null;
    }

    // 查询订单时，先查订单主表，再逐个查询订单明细，组装成页面需要的数据结构
    public List<OrderMain> findUserOrders(Integer userId) {
        List<OrderMain> orders = orderMapper.findByUserId(userId);
        for (OrderMain order : orders) {
            order.setItems(orderMapper.findItemsByOrderId(order.getId()));
        }
        return orders;
    }

    // 将订单状态更新为 2，表示用户确认/支付完成
    public void confirm(Integer id) {
        orderMapper.updateStatus(id, 2);
    }

    // 删除订单主表，order_item 通过外键级联删除
    @Transactional
    public void delete(Integer id) {
        orderMapper.delete(id);
    }
}
