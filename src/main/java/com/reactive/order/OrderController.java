package com.reactive.order;

import com.reactive.order.queue.OrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(description = "returns a product that matches the unique identifier")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}", params = {"identifier"})
    public Mono<OrderRequest> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

}
