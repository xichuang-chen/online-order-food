package com.tw.order.infrastructure.repository.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderEntity {
    private String order_id;
    private String payment;
    private Timestamp create_time;
}
