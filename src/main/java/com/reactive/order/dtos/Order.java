package com.reactive.order.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {
    private Long id;
    private String title;
    private String description;
    private Long price;
    private String currency;
    private String imageUrl;
    private String barcode;
    private String qrCode;
}
