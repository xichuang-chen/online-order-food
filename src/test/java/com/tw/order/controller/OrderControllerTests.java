package com.tw.order.controller;

import com.tw.order.controller.dto.CouponDeductionRequest;
import com.tw.order.controller.dto.CouponDeductionResponse;
import com.tw.order.controller.dto.ResponseBody;
import com.tw.order.controller.dto.User;
import com.tw.order.infrastructure.repository.entity.OrderEntity;
import com.tw.order.service.OrderService;
import com.tw.order.service.model.ResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class OrderControllerTests {

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }
    @Test
    public void givenValidCidWhenRequestPaymentShouldReturnSuccess() {
        OrderController orderController = new OrderController(orderService);
        ResponseBody<OrderEntity> body = new ResponseBody<>("请求成功");
        OrderEntity order = new OrderEntity();
        order.setOrder_id("10001");
        order.setPayment("50");
        order.setCreate_time(new Timestamp(new Date().getTime()));
        body.setData(order);
        when(orderService.payment("10001")).thenReturn(new ResponseModel(HttpStatus.CREATED, body));
        ResponseEntity<ResponseBody> result = orderController.payment("10001");
        assertThat(result.getStatusCodeValue()).isEqualTo(201);
        assertThat(result.getBody().getMessage()).isEqualTo("请求成功");
    }

    @Test
    public void givenInValidCidWhenRequestPaymentShouldReturnNotFound() {
        OrderController orderController = new OrderController(orderService);
        ResponseBody<OrderEntity> body = new ResponseBody<>("订单不存在");
        when(orderService.payment("10002")).thenReturn(new ResponseModel(HttpStatus.NOT_FOUND, body));
        ResponseEntity<ResponseBody> result = orderController.payment("10002");
        assertThat(result.getStatusCodeValue()).isEqualTo(404);
        assertThat(result.getBody().getMessage()).isEqualTo("订单不存在");
    }

    @Test
    public void givenValidCidWhenRequestPaymentAndPaymentSystemErrorShouldReturnServerError() {
        OrderController orderController = new OrderController(orderService);
        ResponseBody<OrderEntity> body = new ResponseBody<>("支付系统错误，请稍后再试");
        when(orderService.payment("10001")).thenReturn(new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, body));
        ResponseEntity<ResponseBody> result = orderController.payment("10001");
        assertThat(result.getStatusCodeValue()).isEqualTo(500);
        assertThat(result.getBody().getMessage()).isEqualTo("支付系统错误，请稍后再试");
    }

    @Test
    public void givenValidCidWhenRequestPaymentAndPaymentSystemTimeoutShouldReturnTimeout() {
        OrderController orderController = new OrderController(orderService);
        ResponseBody<OrderEntity> body = new ResponseBody<>("请求支付系统超时，请稍后再试");
        when(orderService.payment("10001")).thenReturn(new ResponseModel(HttpStatus.GATEWAY_TIMEOUT, body));
        ResponseEntity<ResponseBody> result = orderController.payment("10001");
        assertThat(result.getStatusCodeValue()).isEqualTo(504);
        assertThat(result.getBody().getMessage()).isEqualTo("请求支付系统超时，请稍后再试");
    }


    @Test
    public void givenValidCidWhenRequestCouponDeductionShouldReturnSuccess() {
        OrderController orderController = new OrderController(orderService);
        CouponDeductionResponse response = new CouponDeductionResponse(true, "红包使用成功");
        ResponseBody<CouponDeductionResponse> body = new ResponseBody<>("红包使用成功", response);
        CouponDeductionRequest request = new CouponDeductionRequest(10, new User("9527"));
        when(orderService.couponDeduction("10001", request)).thenReturn(new ResponseModel(HttpStatus.OK, body));
        ResponseEntity<ResponseBody<CouponDeductionResponse>> result = orderController.couponDeduction("10001", request);
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody().getMessage()).isEqualTo("红包使用成功");
        assertThat(result.getBody().getData().isSuccess()).isEqualTo(true);
    }

    @Test
    public void givenInValidCidWhenRequestCouponDeductionShouldReturnNotFound() {
        OrderController orderController = new OrderController(orderService);
        CouponDeductionResponse response = new CouponDeductionResponse(false, "订单不存在");
        ResponseBody<CouponDeductionResponse> body = new ResponseBody<>("订单不存在", response);
        CouponDeductionRequest request = new CouponDeductionRequest(10, new User("9527"));
        when(orderService.couponDeduction("10001", request)).thenReturn(new ResponseModel(HttpStatus.NOT_FOUND, body));
        ResponseEntity<ResponseBody<CouponDeductionResponse>> result = orderController.couponDeduction("10001", request);
        assertThat(result.getStatusCodeValue()).isEqualTo(404);
        assertThat(result.getBody().getMessage()).isEqualTo("订单不存在");
        assertThat(result.getBody().getData().isSuccess()).isEqualTo(false);
    }

    @Test
    public void givenValidCidWhenRequestCouponDeductionAndCouponReturnErrorShouldReturnServerError() {
        OrderController orderController = new OrderController(orderService);
        CouponDeductionResponse response = new CouponDeductionResponse(false, "扣除失败");
        ResponseBody<CouponDeductionResponse> body = new ResponseBody<>("扣除失败", response);
        CouponDeductionRequest request = new CouponDeductionRequest(10, new User("9527"));
        when(orderService.couponDeduction("10001", request)).thenReturn(new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, body));
        ResponseEntity<ResponseBody<CouponDeductionResponse>> result = orderController.couponDeduction("10001", request);
        assertThat(result.getStatusCodeValue()).isEqualTo(500);
        assertThat(result.getBody().getMessage()).isEqualTo("扣除失败");
        assertThat(result.getBody().getData().isSuccess()).isEqualTo(false);
    }

    @Test
    public void givenValidCidWhenRequestCouponDeductionAndCouponTimeoutShouldSendMessageToKafkaAndReturnAccept() {
        OrderController orderController = new OrderController(orderService);
        CouponDeductionResponse response = new CouponDeductionResponse(true, "红包使用成功");
        ResponseBody<CouponDeductionResponse> body = new ResponseBody<>("红包使用成功", response);
        CouponDeductionRequest request = new CouponDeductionRequest(10, new User("9527"));
        when(orderService.couponDeduction("10001", request)).thenReturn(new ResponseModel(HttpStatus.ACCEPTED, body));
        ResponseEntity<ResponseBody<CouponDeductionResponse>> result = orderController.couponDeduction("10001", request);
        assertThat(result.getStatusCodeValue()).isEqualTo(202);
        assertThat(result.getBody().getMessage()).isEqualTo("红包使用成功");
        assertThat(result.getBody().getData().isSuccess()).isEqualTo(true);
    }


}
