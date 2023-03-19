package com.tw.order.infrastructure;

import com.tw.order.controller.dto.CouponDeductionRequest;
import com.tw.order.infrastructure.repository.entity.OrderEntity;
import com.tw.order.service.model.CouponResponse;
import com.tw.order.service.model.PaymentBody;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiClient {

    private final RestTemplate restTemplate;

    public ApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);// 设置建立连接超时时间
        requestFactory.setReadTimeout(60000);// 设置等待返回超时时间
        this.restTemplate.setRequestFactory(requestFactory);
    }

    public PaymentBody payment(OrderEntity order) {
        PaymentBody paymentBody = new PaymentBody();
        paymentBody.setReceiveResponse(false);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "text/html,application/xhtml+xml,application/json;q=0.9,*/*;q=0.8");
        headers.add("Content-Length", "0");
        HttpEntity<OrderEntity> formEntity = new HttpEntity<>(order, headers);
        try {
            ResponseEntity<OrderEntity> response = restTemplate.postForEntity("https://payment.syestem.com/payment", formEntity, OrderEntity.class);
            paymentBody.setReceiveResponse(true);
            if (response.getStatusCodeValue() >= 200 && response.getStatusCodeValue() < 400) {
                paymentBody.setResponse(response.getBody());
            } else {
                paymentBody.setResponse(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentBody;
    }

    public CouponResponse couponDeduction(CouponDeductionRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "text/html,application/xhtml+xml,application/json;q=0.9,*/*;q=0.8");
        headers.add("Content-Length", "0");
        HttpEntity<CouponDeductionRequest> formEntity = new HttpEntity<>(request, headers);
        try {
            ResponseEntity<CouponResponse> response = restTemplate.postForEntity("https://coupon.syestem.com/coupon-deduction", formEntity, CouponResponse.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
