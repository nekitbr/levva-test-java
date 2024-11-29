package com.reactive.order;

import com.reactive.order.queue.OrderRequest;
import com.reactive.order.dtos.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    private static final String PROCESSED_ID_PREFIX = "order_code=";

    /**
     * in an ideal scenario, each step (filter, create order, save products, finish order) would be done by a separate scheduler or queue,
     * but for simplicity’s sake I'm just doing everything at the same place with separate steps as methods.
     */
    @Transactional
    public Mono<OrderEntity> processOrderRequest(OrderRequest orderRequest) {
        return isOrderEligibleToProcess(orderRequest.getFiscalCode())
            .filter(e -> e)
            .flatMap(unused -> markAsReceivedCache(orderRequest.getFiscalCode()))
            .flatMap(unused -> createOrder(orderRequest))
            .flatMap(orderEntity -> saveOrderProducts(orderRequest.getProducts(), orderEntity.getId()).thenReturn(orderEntity))
            .flatMap(this::finishOrderProcess);
    }

    public Mono<OrderRequest> getOrderById(Long key) {
        return orderRepository
            .findById(key)
            .map(this::mapToResponse);
    }

    private Mono<Boolean> isReceivedCache(String code) {
        return redisTemplate.opsForValue()
            .get(PROCESSED_ID_PREFIX + code)
            .map(existingValue -> {
                log.info("code " + code + " exists!");
                return true;
            })
            .defaultIfEmpty(false);
    }

    private Mono<Boolean> markAsReceivedCache(String code) {
        return redisTemplate.opsForValue()
            .set(PROCESSED_ID_PREFIX + code, "processed")
            .then(redisTemplate.expire(PROCESSED_ID_PREFIX + code, Duration.ofHours(1)));
    }

    private Mono<Boolean> isOrderEligibleToProcess(String fiscalCode) {
        return orderRepository.existsByFiscalCode(fiscalCode) // if not, exists in database long-term storage?
            .map(exists -> !exists) // if not then is eligible
            .switchIfEmpty(Mono.just(false));
    }

    private Mono<OrderEntity> finishOrderProcess(OrderEntity orderEntity) {
        orderEntity.setStatus(OrderStatus.FINISHED);
        return orderRepository.save(orderEntity);
    }

    private Mono<Void> saveOrderProducts(List<Product> products, Long orderId) {
        return Flux.fromIterable(products)
            .flatMap(product -> {
                var unsavedOrderProduct = OrderProductEntity.builder()
                    .orderId(orderId)
                    .name(product.getName())
                    .quantity(product.getQuantity())
                    .price(product.getPrice())
                    .build();

                return orderProductRepository.save(unsavedOrderProduct);
            })
            .then();
    }

    private Mono<OrderEntity> createOrder(OrderRequest orderRequest) {
        var unsavedOrder = OrderEntity.builder()
            .fiscalCode(orderRequest.getFiscalCode())
            .price(orderRequest.getProducts().stream().mapToDouble(Product::getPrice).sum())
            .currency(orderRequest.getCurrency().getCurrencyCode())
            .status(OrderStatus.CREATED)
            .customerId(orderRequest.getCustomerId())
            .build();

        return orderRepository.save(unsavedOrder);
    }

    private OrderRequest mapToResponse(OrderEntity orderEntity) {
        return OrderRequest.builder()
            .fiscalCode(orderEntity.getFiscalCode())
            .build();
    }

}
