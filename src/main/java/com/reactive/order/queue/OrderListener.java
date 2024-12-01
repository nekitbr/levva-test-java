package com.reactive.order.queue;

import com.reactive.config.rabbitmq.RabbitMQConfig;
import com.reactive.order.OrderEntity;
import com.reactive.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderListener {

    private final OrderService orderService;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    private static final String PROCESSED_ID_PREFIX = "order_code=";

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public Mono<Void> processOrderRequest(OrderRequest orderRequest) {
        return wasAlreadyReceived(orderRequest.getFiscalCode())
            .filter(e -> !e)
            .flatMap(unused -> markAsReceivedCache(orderRequest.getFiscalCode()))
            .flatMap(unused -> orderService.processOrderRequest(orderRequest))
            .onErrorResume(DuplicateKeyException.class, ex -> handleDuplicateKeyException(orderRequest, ex))
            .onErrorResume(RuntimeException.class, ex -> handleRuntimeException(orderRequest, ex))
            .then();
    }

    private static Mono<OrderEntity> handleDuplicateKeyException(OrderRequest orderRequest, DuplicateKeyException exception) {
        log.error(MessageFormat.format("Deduplication Failure on orderCode={0} processing: {1}",
            orderRequest.getFiscalCode(), exception.getCause().getLocalizedMessage()));
        return Mono.empty();
    }

    private Mono<OrderEntity> handleRuntimeException(OrderRequest orderRequest, RuntimeException exception) {
        log.error(MessageFormat.format("Process Failure on orderCode={0}: {1}",
            orderRequest.getFiscalCode(), exception.getMessage()));

        return removeFromCache(orderRequest.getFiscalCode())
            .flatMap(e -> Mono.error(exception));
    }

    private Mono<Boolean> wasAlreadyReceived(String code) {
        return redisTemplate.opsForValue()
            .get(PROCESSED_ID_PREFIX + code)
            .map(existingValue -> true)
            .defaultIfEmpty(false);
    }

    private Mono<Boolean> markAsReceivedCache(String code) {
        return redisTemplate.opsForValue()
            .set(PROCESSED_ID_PREFIX + code, "processed")
            .then(redisTemplate.expire(PROCESSED_ID_PREFIX + code, Duration.ofHours(1)));
    }

    private Mono<Long> removeFromCache(String code) {
        return redisTemplate.delete(PROCESSED_ID_PREFIX + code, "processed");
    }

}
