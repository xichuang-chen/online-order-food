package com.tw.order.infrastructure.repository;

import com.tw.order.infrastructure.repository.entity.OrderEntity;

import java.util.List;

public class OrderMapperFaker implements OrderMapper {
    @Override
    public OrderEntity findOrderById(String orderId) {
        OrderEntity order = new OrderEntity();
        order.setOrder_id("10001");
        order.setPayment("50");
        return orderId.equals("10001") ? order : null;
    }

    @Override
    public List<OrderEntity> findAllOrders() {
        return null;
    }

    @Override
    public Integer deleteOrderById(Integer orderId) {
        return null;
    }

    @Override
    public Integer addOrder(OrderEntity order) {
        return null;
    }

    @Override
    public Integer updateOrder(OrderEntity Person) {
        return null;
    }
}
