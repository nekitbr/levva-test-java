package com.reactive.order;

import com.reactive.product.Product;
import lombok.Builder;
import lombok.Data;

import java.util.Currency;
import java.util.List;

@Data
@Builder
public class Order {
    private Long id;
    private String fiscalCode;
    private OrderStatus status;
    private Double price;
    private Currency currency;
    private Long customerId;
    private List<Product> products;
}
