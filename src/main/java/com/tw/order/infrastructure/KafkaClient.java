package com.tw.order.infrastructure;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaClient {

    private KafkaTemplate<String,Object> kafkaTemplate;
    public static String data = "init";

    public KafkaClient(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public <T> void send(String topic, T msg) {
        String json = JSON.toJSONString(msg);
        kafkaTemplate.send(topic, json);
    }

    @KafkaListener(topics = "order-coupon")
    public void listen (ConsumerRecord<?, ?> record){
        data = record.value().toString();
    }
}
