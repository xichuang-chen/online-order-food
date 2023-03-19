package com.tw.order.infrastructure.repository;

import com.tw.order.controller.dto.OrderDetails;
import com.tw.order.infrastructure.repository.entity.OrderEntity;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    private final OrderMapper orderMapper;

    public OrderRepository(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public OrderEntity getOrderByCid(String cid) {
        OrderEntity order = orderMapper.findOrderById(cid);
        return transformPo(order);
    }

    public OrderEntity transformPo(OrderEntity order) {
        if (order == null) {
            return null;
        }
        return order;
    }
}
