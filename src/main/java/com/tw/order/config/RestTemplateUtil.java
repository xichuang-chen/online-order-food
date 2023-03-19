package com.tw.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@MapperScan(basePackages = "com.tw.order.infrastructure.repository")
public class RestTemplateUtil {

    @Bean
    public RestTemplate createRestTemplate() {
        return new RestTemplate();
    }
}
