package com.tw.order.infrastructure.repository;

import com.tw.order.infrastructure.repository.entity.OrderEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Select(value = {"select * from orders where order_id=#{orderId}"})
    OrderEntity findOrderById(String orderId);

    @Select(value = {"select * from orders"})
    List<OrderEntity> findAllOrders();

    @Delete("delete from orders where order_id = #{orderId}")
    Integer deleteOrderById(Integer orderId);

    @Insert("insert into orders(order_id,payment,crate_time) values(#{orderId},#{payment},#{createTime})")
    Integer addOrder(OrderEntity order);

    @Update("update orders set order_id=#{orderId},payment=#{payment},create_time=#{createTime} where order_id=#{orderId}")
    Integer updateOrder(OrderEntity Person);

}
