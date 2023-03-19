package com.tw.order.service.model;

import com.tw.order.infrastructure.repository.entity.OrderEntity;
import lombok.Data;

@Data
public class PaymentBody {
    private boolean receiveResponse;
    private OrderEntity response;
}
