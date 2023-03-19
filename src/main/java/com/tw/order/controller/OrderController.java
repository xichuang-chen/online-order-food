package com.tw.order.controller;

import com.tw.order.controller.dto.CouponDeductionRequest;
import com.tw.order.service.OrderService;
import com.tw.order.service.model.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/foods-purchase-contracts/{cid}/payment")
    public ResponseEntity payment(@PathVariable String cid) {
        ResponseModel response = orderService.payment(cid);
        return new ResponseEntity<>(response.getDetails(), response.getStatus());
    }

    @PostMapping("/foods-purchase-contracts/{cid}/coupon-deduction")
    public ResponseEntity couponDeduction(@PathVariable String cid, @RequestBody CouponDeductionRequest request) {
        ResponseModel response = orderService.couponDeduction(cid, request);
        return new ResponseEntity<>(response.getDetails(), response.getStatus());
    }

    @GetMapping("/test")
    public ResponseEntity test() {
        return ResponseEntity.ok("success~~");
    }


}
