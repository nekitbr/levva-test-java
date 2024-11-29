package com.reactive.order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository extends R2dbcRepository<OrderEntity, Long> {

    Mono<Boolean> existsByFiscalCode(String fiscalCode);
    Flux<OrderEntity> findAllBy(Pageable pageable);

}
