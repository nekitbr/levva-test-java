package com.reactive.order.queue;

import com.reactive.config.rabbitmq.RabbitMQConfig;
import com.reactive.order.dtos.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderListener {

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void listen(Order order) {
        log.info("Received order: {}", order);
    }
}
