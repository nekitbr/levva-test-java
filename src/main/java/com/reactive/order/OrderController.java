package com.reactive.order;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(description = "returns an order that matches the given id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    public Mono<OrderEntity> getOrderById(@PathVariable String id) {
        return orderService.getOrderById(id);
    }

    @Operation(description = "returns all orders within given period, pageable")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Flux<OrderEntity> getOrders(@RequestParam int page, @RequestParam int size) {
        return orderService.getOrdersByPage(page, size);
    }

    @Operation(description = "returns an order that matches the given fiscal code")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/fiscalcode/{fiscalCode}")
    public Mono<OrderEntity> getOrderByFiscalCode(@PathVariable String fiscalCode) {
        return orderService.getOrderByFiscalCode(fiscalCode);
    }
}
