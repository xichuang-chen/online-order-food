package com.tw.order.service;

import com.tw.order.controller.dto.CouponDeductionRequest;
import com.tw.order.controller.dto.CouponDeductionResponse;
import com.tw.order.controller.dto.ResponseBody;
import com.tw.order.infrastructure.ApiClient;
import com.tw.order.infrastructure.KafkaClient;
import com.tw.order.infrastructure.repository.OrderRepository;
import com.tw.order.infrastructure.repository.entity.OrderEntity;
import com.tw.order.service.model.CouponResponse;
import com.tw.order.service.model.PaymentBody;
import com.tw.order.service.model.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final KafkaClient kafkaClient;
    private final ApiClient apiClient;
    private final OrderRepository orderRepository;

    public OrderService(KafkaClient kafkaClient, ApiClient apiClient, OrderRepository orderRepository) {
        this.kafkaClient = kafkaClient;
        this.orderRepository = orderRepository;
        this.apiClient = apiClient;
    }


    public ResponseModel payment(String cid) {
        OrderEntity order = orderRepository.getOrderByCid(cid);
        if (order == null) {
            return new ResponseModel<>(HttpStatus.NOT_FOUND, new ResponseBody<>("订单不存在"));
        }
        PaymentBody orderFromPaymentSystem = apiClient.payment(order);
        if (!orderFromPaymentSystem.isReceiveResponse()) {
            return new ResponseModel(HttpStatus.GATEWAY_TIMEOUT, new ResponseBody("请求支付系统超时，请稍后再试"));
        }
        if (orderFromPaymentSystem.getResponse() == null) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, new ResponseBody("支付系统错误，请稍后再试"));
        }
        ResponseBody<OrderEntity> body = new ResponseBody<>("请求成功");
        body.setData(orderFromPaymentSystem.getResponse());
        return new ResponseModel(HttpStatus.CREATED, body);
    }

    public ResponseModel couponDeduction(String cid, CouponDeductionRequest request) {
        OrderEntity order = orderRepository.getOrderByCid(cid);
        CouponDeductionResponse coupon = new CouponDeductionResponse(false, "订单不存在");
        if (order == null) {
            return new ResponseModel<>(HttpStatus.NOT_FOUND, new ResponseBody<>("订单不存在", coupon));
        }
        CouponResponse response = apiClient.couponDeduction(request);

        if (response == null) {
            kafkaClient.send("order-coupon", request);
            CouponDeductionResponse couponDeductionResponse = new CouponDeductionResponse(true, "红包使用成功");
            return new ResponseModel(HttpStatus.ACCEPTED, new ResponseBody<>("红包使用成功", couponDeductionResponse));
        }

        if (response.isDeduct()) {
            CouponDeductionResponse couponDeductionResponse = new CouponDeductionResponse(true, response.getMessage());
            return new ResponseModel(HttpStatus.OK, new ResponseBody<>("红包使用成功", couponDeductionResponse));
        }
        CouponDeductionResponse couponDeductionResponse = new CouponDeductionResponse(false, response.getMessage());
        return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, new ResponseBody<>("服务错误，请稍后再试", couponDeductionResponse));
    }


}
