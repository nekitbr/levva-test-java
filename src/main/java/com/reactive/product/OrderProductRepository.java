package com.reactive.product;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderProductRepository extends R2dbcRepository<OrderProductEntity, Long> {

    Flux<Product> findAllByOrderId(Long orderId);

}
