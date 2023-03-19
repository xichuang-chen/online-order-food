package com.tw.order.controller.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDetails {
    private String cid;
    private String payment;
    private Date createDate;
}
