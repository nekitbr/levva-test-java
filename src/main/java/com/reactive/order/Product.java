package com.reactive.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Indexed
    private Long id;
    private String name;
    private Double price;
    private Long quantity;

}
