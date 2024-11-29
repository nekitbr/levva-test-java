package com.reactive.order.dtos;

import lombok.Data;

@Data
public class Product {
    private String name;
    private Double price;
    private Long quantity;
}
