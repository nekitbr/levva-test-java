package com.reactive.order;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends R2dbcRepository<OrderProductEntity, Long> {
}
