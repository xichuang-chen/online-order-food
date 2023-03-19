package com.tw.order.infrastructure.repository;


import com.tw.order.infrastructure.KafkaClient;
import com.tw.order.infrastructure.repository.entity.OrderEntity;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


//@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class OrderRepositoryTests {

    @MockBean
    private KafkaClient kafkaClient;

    @MockBean
    private KafkaTemplate kafkaTemplate;


    @Before
    public void setUp() {

    }

    @Test
    public void testGetAllPerson() {
        OrderMapperFaker orderMapperFaker = new OrderMapperFaker();
        OrderEntity order = orderMapperFaker.findOrderById("10001");
        assertThat(order.getOrder_id()).isEqualTo("10001");
    }

    @Test
    public void givenValidCidWhenQuerySqlShouldReturnOrder() {
        OrderMapperFaker orderMapperFaker = new OrderMapperFaker();
        OrderRepository orderRepository = new OrderRepository(orderMapperFaker);
        OrderEntity order = new OrderEntity();
        order.setOrder_id("10001");
        order.setPayment("50");
        assertThat(orderRepository.getOrderByCid("10001")).isEqualTo(order);
    }

    @Test
    public void givenInValidCidWhenQuerySqlShouldReturnNull() {
        OrderMapperFaker orderMapperFaker = new OrderMapperFaker();
        OrderRepository orderRepository = new OrderRepository(orderMapperFaker);
        OrderEntity order = new OrderEntity();
        order.setOrder_id("10001");
        order.setPayment("50");
        assertThat(orderRepository.getOrderByCid("10002")).isEqualTo(null);
    }

}
