package com.reactive.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final OrderProductRepository orderProductRepository;

    public Flux<Product> getProductsByOrderId(Long orderId) {
        return orderProductRepository.findAllByOrderId(orderId);
    }

}
