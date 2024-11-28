package com.reactive.order;

import com.reactive.order.dtos.Order;
import com.reactive.order.dtos.OrderIdentifier;
import com.reactive.order.dtos.OrderPatchRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class OrderController {

    private final OrderService orderService;

    @Operation(description = "adds the product in the inventory")
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public Mono<Order> saveNewProduct(@RequestBody @Valid Order recipeRequest) {
        return orderService.saveNewProduct(recipeRequest);
    }

    @Operation(description = "returns all products")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Flux<Order> getAllProducts() {
        return orderService.getProducts();
    }

    @Operation(description = "returns a product that matches the unique identifier")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{key}", params = {"identifier"})
    public Mono<Order> getProductByIdentifier(@PathVariable String key,
                                              @RequestParam @Valid OrderIdentifier identifier) {
        return orderService.getProductByIdentifier(key, identifier);
    }

    @Operation(description = "adds a new product image by ID. Does not work with barcode or QRCode")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/image/{id}")
    public Mono<Void> patchProductImageUrl(@PathVariable Long id, @RequestBody OrderPatchRequest req) {
        return orderService.patchProductImage(id, req);
    }

    @Operation(description = "deletes a product by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable Long id) {
        return orderService.deleteProductById(id);
    }

}
