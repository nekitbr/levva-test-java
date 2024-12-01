package com.reactive.order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepository extends ReactiveMongoRepository<OrderEntity, String> {

    Mono<Boolean> existsByFiscalCode(String fiscalCode);

    Mono<OrderEntity> findByFiscalCode(String fiscalCode);
    Flux<OrderEntity> findAllBy(Pageable pageable);

}
