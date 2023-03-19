package com.tw.order.infrastructure;


import com.tw.order.controller.dto.CouponDeductionRequest;
import com.tw.order.controller.dto.User;
import com.tw.order.infrastructure.repository.entity.OrderEntity;
import com.tw.order.service.model.PaymentBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class KafkaClientTests {

    @Mock
    private KafkaTemplate<String,Object> kafkaTemplate;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenOrderIdAndRequestMessageWhenUseKafkaClientSendMessageShouldSendMessageToKafka() {
        KafkaClient kafkaClient = new KafkaClient(kafkaTemplate);
        CouponDeductionRequest request = new CouponDeductionRequest(20, new User("9527"));

        ArgumentCaptor<String> topicArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<CouponDeductionRequest> requestArgumentCaptor = ArgumentCaptor.forClass(CouponDeductionRequest.class);

        kafkaClient.send("topic-test", request);
        verify(kafkaTemplate, times(1)).send(topicArgumentCaptor.capture(), requestArgumentCaptor.capture());
        assertThat(topicArgumentCaptor.getValue()).isEqualTo("topic-test");
        assertThat(requestArgumentCaptor.getValue()).isEqualTo("{\"couponAmount\":20,\"user\":{\"userId\":\"9527\"}}");

    }
}

