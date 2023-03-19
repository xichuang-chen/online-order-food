package com.tw.order.infrastructure;


import com.tw.order.controller.dto.CouponDeductionRequest;
import com.tw.order.controller.dto.User;
import com.tw.order.infrastructure.repository.entity.OrderEntity;
import com.tw.order.service.model.CouponResponse;
import com.tw.order.service.model.PaymentBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class ApiClientTests {

    @Mock
    private RestTemplate restTemplate;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenPaymentOrderWhenRequestPaymentShouldReturnSuccess() {
        ApiClient apiClient = new ApiClient(restTemplate);
        OrderEntity order = new OrderEntity();
        order.setOrder_id("10001");
        order.setPayment("50");
        order.setCreate_time(new Timestamp(new Date().getTime()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "text/html,application/xhtml+xml,application/json;q=0.9,*/*;q=0.8");
        headers.add("Content-Length", "0");
        HttpEntity<OrderEntity> formEntity = new HttpEntity<>(order, headers);
        when(restTemplate.postForEntity("https://payment.syestem.com/payment", formEntity, OrderEntity.class))
                .thenReturn(ResponseEntity.ok(order));
        PaymentBody paymentBody = apiClient.payment(order);
        assertThat(paymentBody.isReceiveResponse()).isEqualTo(true);
        assertThat(paymentBody.getResponse()).isEqualTo(order);

    }

    @Test
    public void givenPaymentOrderWhenRequestPaymentAndPaymentSystemErrorShouldReturnPaymentBodyNull() {
        ApiClient apiClient = new ApiClient(restTemplate);
        OrderEntity order = new OrderEntity();
        order.setOrder_id("10001");
        order.setPayment("50");
        order.setCreate_time(new Timestamp(new Date().getTime()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "text/html,application/xhtml+xml,application/json;q=0.9,*/*;q=0.8");
        headers.add("Content-Length", "0");
        HttpEntity<OrderEntity> formEntity = new HttpEntity<>(order, headers);
        when(restTemplate.postForEntity("https://payment.syestem.com/payment", formEntity, OrderEntity.class))
                .thenReturn(ResponseEntity.internalServerError().build());
        PaymentBody paymentBody = apiClient.payment(order);
        assertThat(paymentBody.isReceiveResponse()).isEqualTo(true);
        assertThat(paymentBody.getResponse()).isEqualTo(null);
    }

    @Test
    public void givenPaymentOrderWhenRequestPaymentAndPaymentSystemTimeoutShouldReturnReceiveResponseFalse() {
        ApiClient apiClient = new ApiClient(restTemplate);
        OrderEntity order = new OrderEntity();
        order.setOrder_id("10001");
        order.setPayment("50");
        order.setCreate_time(new Timestamp(new Date().getTime()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "text/html,application/xhtml+xml,application/json;q=0.9,*/*;q=0.8");
        headers.add("Content-Length", "0");
        HttpEntity<OrderEntity> formEntity = new HttpEntity<>(order, headers);
        when(restTemplate.postForEntity("https://payment.syestem.com/payment", formEntity, OrderEntity.class))
                .thenThrow(new RuntimeException());
        PaymentBody paymentBody = apiClient.payment(order);
        assertThat(paymentBody.isReceiveResponse()).isEqualTo(false);
        assertThat(paymentBody.getResponse()).isEqualTo(null);
    }


    @Test
    public void givenCouponDeductionRequestWhenRequestCouponSystemShouldReturnSuccess() {
        ApiClient apiClient = new ApiClient(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "text/html,application/xhtml+xml,application/json;q=0.9,*/*;q=0.8");
        headers.add("Content-Length", "0");
        CouponDeductionRequest request = new CouponDeductionRequest(20, new User("9527"));
        HttpEntity<CouponDeductionRequest> formEntity = new HttpEntity<>(request, headers);
        CouponResponse couponResponse = new CouponResponse(true, "红包使用成功");
        when(restTemplate.postForEntity("https://coupon.syestem.com/coupon-deduction", formEntity, CouponResponse.class))
                .thenReturn(ResponseEntity.ok(couponResponse));
        CouponResponse response = apiClient.couponDeduction(request);
        assertThat(response.isDeduct()).isEqualTo(true);
        assertThat(response.getMessage()).isEqualTo("红包使用成功");

    }

    @Test
    public void givenCouponDeductionRequestWhenRequestAndCouponSystemErrorShouldReturnIsDeductFalse() {
        ApiClient apiClient = new ApiClient(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "text/html,application/xhtml+xml,application/json;q=0.9,*/*;q=0.8");
        headers.add("Content-Length", "0");
        CouponDeductionRequest request = new CouponDeductionRequest(20, new User("9527"));
        HttpEntity<CouponDeductionRequest> formEntity = new HttpEntity<>(request, headers);
        CouponResponse couponResponse = new CouponResponse(false, "红包扣除失败");
        when(restTemplate.postForEntity("https://coupon.syestem.com/coupon-deduction", formEntity, CouponResponse.class))
                .thenReturn(ResponseEntity.ok(couponResponse));
        CouponResponse response = apiClient.couponDeduction(request);
        assertThat(response.isDeduct()).isEqualTo(false);
        assertThat(response.getMessage()).isEqualTo("红包扣除失败");
    }

    @Test
    public void givenCouponDeductionRequestWhenRequestAndCouponTimeoutShouldReturnNull() {

        ApiClient apiClient = new ApiClient(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "text/html,application/xhtml+xml,application/json;q=0.9,*/*;q=0.8");
        headers.add("Content-Length", "0");
        CouponDeductionRequest request = new CouponDeductionRequest(20, new User("9527"));
        HttpEntity<CouponDeductionRequest> formEntity = new HttpEntity<>(request, headers);
        when(restTemplate.postForEntity("https://coupon.syestem.com/coupon-deduction", formEntity, CouponResponse.class))
                .thenThrow(new RuntimeException());
        CouponResponse response = apiClient.couponDeduction(request);
        assertThat(response).isEqualTo(null);
    }
}
