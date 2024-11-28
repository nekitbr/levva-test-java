package com.reactive.order;

import com.reactive.order.dtos.Order;
import com.reactive.order.dtos.OrderIdentifier;
import com.reactive.order.dtos.OrderPatchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.reactive.order.dtos.OrderIdentifier.ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Flux<Order> getProducts() {
        return orderRepository
                .findAll()
                .map(this::mapToResponse);
    }

    private Mono<OrderEntity> getProductEntityById(Long key) {
        return getProductEntityByIdentifier(String.valueOf(key), ID);
    }

    public Mono<Order> getProductByIdentifier(String key, OrderIdentifier identifier) {
        return getProductEntityByIdentifier(key, identifier)
                .map(this::mapToResponse);
    }

    public Mono<Void> deleteProductById(Long id) {
        return orderRepository.deleteById(id);
    }

    private Mono<OrderEntity> getProductEntityByIdentifier(String key, OrderIdentifier identifier) {
        return switch (identifier) {
            case ID -> orderRepository.findById(Long.parseLong(key));
//            case BARCODE -> productTenantRepository.findByBarcode(key);
//            case QRCODE -> productTenantRepository.findByQrCode(key);
            case null, default -> Mono.empty();
        };
    }

    @Transactional
    public Mono<Order> saveNewProduct(Order order) {
        return orderRepository
                .save(mapToEntity(order))
                .map(this::mapToResponse);
    }

    @Transactional
    public Mono<Void> patchProductImage(Long id, OrderPatchRequest patchProductImage) {
        return getProductEntityById(id)
                .map(product -> {
                    product.setImageUrl(patchProductImage.getImageUrl());
                    return product;
                })
                .flatMap(orderRepository::save)
                .then();
    }

    private OrderEntity mapToEntity(Order orderDTO) {
        return OrderEntity.builder()
                .id(orderDTO.getId())
                .title(orderDTO.getTitle())
                .description(orderDTO.getDescription())
                .price(orderDTO.getPrice())
                .currency(orderDTO.getCurrency())
                .imageUrl(orderDTO.getImageUrl())
                .build();
    }

    private Order mapToResponse(OrderEntity orderEntity) {
        return Order.builder()
                .id(orderEntity.getId())
                .title(orderEntity.getTitle())
                .description(orderEntity.getDescription())
                .price(orderEntity.getPrice())
                .currency(orderEntity.getCurrency())
                .imageUrl(orderEntity.getImageUrl())
                .build();
    }

}
