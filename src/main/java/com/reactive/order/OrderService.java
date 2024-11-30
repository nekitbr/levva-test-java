package com.reactive.order;

import com.reactive.order.queue.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Mono<OrderEntity> processOrderRequest(OrderRequest orderRequest) {
        return isOrderEligibleToProcess(orderRequest.getFiscalCode())
            .filter(e -> e)
            .flatMap(unused -> createOrder(orderRequest));
    }

    public Mono<OrderEntity> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Flux<OrderEntity> getOrdersByPage(int page, int size) {
        return orderRepository.findAllBy(PageRequest.of(page, size));
    }

    private Mono<Boolean> isOrderEligibleToProcess(String fiscalCode) {
        return orderRepository.existsByFiscalCode(fiscalCode) // exists in database long-term storage?
            .map(exists -> !exists); // if not then is eligible
    }

    private Mono<OrderEntity> createOrder(OrderRequest orderRequest) {
        var unsavedOrder = OrderEntity.builder()
            .fiscalCode(orderRequest.getFiscalCode())
            .price(orderRequest.getProducts().stream().mapToDouble(Product::getPrice).sum())
            .currency(orderRequest.getCurrency().getCurrencyCode())
            .status(OrderStatus.FINISHED)
            .customerId(orderRequest.getCustomerId())
            .products(orderRequest.getProducts())
            .build();

        return orderRepository.save(unsavedOrder);
    }

}
