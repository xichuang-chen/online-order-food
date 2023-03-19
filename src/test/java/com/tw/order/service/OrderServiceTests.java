package com.tw.order.service;


import com.tw.order.controller.dto.CouponDeductionRequest;
import com.tw.order.controller.dto.CouponDeductionResponse;
import com.tw.order.controller.dto.ResponseBody;
import com.tw.order.controller.dto.User;
import com.tw.order.infrastructure.ApiClient;
import com.tw.order.infrastructure.KafkaClient;
import com.tw.order.infrastructure.repository.OrderRepository;
import com.tw.order.infrastructure.repository.entity.OrderEntity;
import com.tw.order.service.model.CouponResponse;
import com.tw.order.service.model.PaymentBody;
import com.tw.order.service.model.ResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class OrderServiceTests {

    @Mock
    private KafkaClient kafkaClient;
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ApiClient apiClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenValidCidWhenRequestPaymentShouldReturnSuccess() {
        OrderService orderService = new OrderService(kafkaClient, apiClient, orderRepository);
        OrderEntity order = new OrderEntity();
        order.setOrder_id("10001");
        order.setPayment("50");
        order.setCreate_time(new Timestamp(new Date().getTime()));
        when(orderRepository.getOrderByCid("10001")).thenReturn(order);
        PaymentBody paymentBody = new PaymentBody();
        paymentBody.setResponse(order);
        paymentBody.setReceiveResponse(true);
        when(apiClient.payment(order)).thenReturn(paymentBody);
        ResponseModel result = orderService.payment("10001");
        assertThat(result.getStatus().value()).isEqualTo(201);
        assertThat(result.getDetails().getMessage()).isEqualTo("请求成功");
    }

    @Test
    public void givenInValidCidWhenRequestPaymentShouldReturnNotFound() {
        OrderService orderService = new OrderService(kafkaClient, apiClient, orderRepository);
        OrderEntity order = new OrderEntity();
        order.setOrder_id("10001");
        order.setPayment("50");
        order.setCreate_time(new Timestamp(new Date().getTime()));
        when(orderRepository.getOrderByCid("10001")).thenReturn(null);
        PaymentBody paymentBody = new PaymentBody();
        paymentBody.setResponse(order);
        paymentBody.setReceiveResponse(true);
        when(apiClient.payment(order)).thenReturn(paymentBody);
        ResponseModel result = orderService.payment("10001");
        assertThat(result.getStatus().value()).isEqualTo(404);
        assertThat(result.getDetails().getMessage()).isEqualTo("订单不存在");
    }

    @Test
    public void givenValidCidWhenRequestPaymentAndPaymentSystemErrorShouldReturnServerError() {
        OrderService orderService = new OrderService(kafkaClient, apiClient, orderRepository);
        OrderEntity order = new OrderEntity();
        order.setOrder_id("10001");
        order.setPayment("50");
        order.setCreate_time(new Timestamp(new Date().getTime()));
        when(orderRepository.getOrderByCid("10001")).thenReturn(order);
        PaymentBody paymentBody = new PaymentBody();
        paymentBody.setResponse(null);
        paymentBody.setReceiveResponse(true);
        when(apiClient.payment(order)).thenReturn(paymentBody);
        ResponseModel result = orderService.payment("10001");
        assertThat(result.getStatus().value()).isEqualTo(500);
        assertThat(result.getDetails().getMessage()).isEqualTo("支付系统错误，请稍后再试");
    }

    @Test
    public void givenValidCidWhenRequestPaymentAndPaymentSystemTimeoutShouldReturnTimeout() {
        OrderService orderService = new OrderService(kafkaClient, apiClient, orderRepository);
        OrderEntity order = new OrderEntity();
        order.setOrder_id("10001");
        order.setPayment("50");
        order.setCreate_time(new Timestamp(new Date().getTime()));
        when(orderRepository.getOrderByCid("10001")).thenReturn(order);
        PaymentBody paymentBody = new PaymentBody();
        paymentBody.setReceiveResponse(false);
        when(apiClient.payment(order)).thenReturn(paymentBody);
        ResponseModel result = orderService.payment("10001");
        assertThat(result.getStatus().value()).isEqualTo(504);
        assertThat(result.getDetails().getMessage()).isEqualTo("请求支付系统超时，请稍后再试");
    }

    @Test
    public void givenValidCidWhenRequestCouponDeductionShouldReturnSuccess() {
        OrderService orderService = new OrderService(kafkaClient, apiClient, orderRepository);
        OrderEntity order = new OrderEntity();
        order.setOrder_id("10001");
        order.setPayment("50");
        order.setCreate_time(new Timestamp(new Date().getTime()));
        when(orderRepository.getOrderByCid("10001")).thenReturn(order);
        CouponDeductionRequest request = new CouponDeductionRequest(20, new User("9527"));
        CouponResponse couponResponse = new CouponResponse(true, "红包使用成功");
        when(apiClient.couponDeduction(request)).thenReturn(couponResponse);
        ResponseModel<CouponDeductionResponse> result = orderService.couponDeduction("10001", request);
        assertThat(result.getStatus().value()).isEqualTo(200);
        assertThat(result.getDetails().getMessage()).isEqualTo("红包使用成功");
        assertThat(result.getDetails().getData().isSuccess()).isEqualTo(true);
    }

    @Test
    public void givenInValidCidWhenRequestCouponDeductionShouldReturnNotFound() {
        OrderService orderService = new OrderService(kafkaClient, apiClient, orderRepository);
        when(orderRepository.getOrderByCid("10001")).thenReturn(null);
        CouponDeductionRequest request = new CouponDeductionRequest(20, new User("9527"));
        ResponseModel<CouponDeductionResponse> result = orderService.couponDeduction("10001", request);
        assertThat(result.getStatus().value()).isEqualTo(404);
        assertThat(result.getDetails().getMessage()).isEqualTo("订单不存在");
        assertThat(result.getDetails().getData().isSuccess()).isEqualTo(false);
    }

    @Test
    public void givenValidCidWhenRequestCouponDeductionAndCouponReturnErrorShouldReturnServerError() {
        OrderService orderService = new OrderService(kafkaClient, apiClient, orderRepository);
        OrderEntity order = new OrderEntity();
        order.setOrder_id("10001");
        order.setPayment("50");
        order.setCreate_time(new Timestamp(new Date().getTime()));
        when(orderRepository.getOrderByCid("10001")).thenReturn(order);
        CouponDeductionRequest request = new CouponDeductionRequest(20, new User("9527"));
        CouponResponse couponResponse = new CouponResponse(false, "扣除失败");
        when(apiClient.couponDeduction(request)).thenReturn(couponResponse);
        ResponseModel<CouponDeductionResponse> result = orderService.couponDeduction("10001", request);
        assertThat(result.getStatus().value()).isEqualTo(500);
        assertThat(result.getDetails().getMessage()).isEqualTo("服务错误，请稍后再试");
        assertThat(result.getDetails().getData().isSuccess()).isEqualTo(false);
    }

    @Test
    public void givenValidCidWhenRequestCouponDeductionAndCouponTimeoutShouldSendMessageToKafkaAndReturnAccept() {
        OrderService orderService = new OrderService(kafkaClient, apiClient, orderRepository);
        OrderEntity order = new OrderEntity();
        order.setOrder_id("10001");
        order.setPayment("50");
        order.setCreate_time(new Timestamp(new Date().getTime()));
        when(orderRepository.getOrderByCid("10001")).thenReturn(order);
        CouponDeductionRequest request = new CouponDeductionRequest(20, new User("9527"));
        when(apiClient.couponDeduction(request)).thenReturn(null);
        Mockito.doNothing().when(kafkaClient).send("order-coupon", request);
        ResponseModel<CouponDeductionResponse> result = orderService.couponDeduction("10001", request);
        assertThat(result.getStatus().value()).isEqualTo(202);
        assertThat(result.getDetails().getMessage()).isEqualTo("红包使用成功");
        assertThat(result.getDetails().getData().isSuccess()).isEqualTo(true);
    }
}
