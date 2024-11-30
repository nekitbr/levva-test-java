package com.reactive.order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepository extends ReactiveMongoRepository<OrderEntity, Long> {

    Mono<Boolean> existsByFiscalCode(String fiscalCode);

    Flux<OrderEntity> findAllBy(Pageable pageable);

}
