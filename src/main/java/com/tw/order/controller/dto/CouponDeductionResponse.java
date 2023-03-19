package com.tw.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CouponDeductionResponse {
    private boolean isSuccess;
    private String message;
}
