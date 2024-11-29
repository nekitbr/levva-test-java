package com.reactive.order.queue;

import com.reactive.order.dtos.Product;
import lombok.Builder;
import lombok.Data;

import java.util.Currency;
import java.util.List;

@Data
@Builder
public class OrderRequest {
    private String fiscalCode;
    private List<Product> products;
    private Currency currency;
    private Long customerId;
}
