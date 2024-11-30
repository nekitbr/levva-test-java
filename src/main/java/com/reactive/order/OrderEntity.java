package com.reactive.order;

import com.reactive.config.mongodb.CustomAuditMongo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class OrderEntity extends CustomAuditMongo implements Serializable {

    @Id
    private String id;
    @Indexed(unique = true)
    private String fiscalCode;
    private OrderStatus status;
    private Double price;
    private String currency;
    private List<Product> products;
    private Long customerId;

}
