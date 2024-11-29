package com.reactive.order;

import com.reactive.config.audit.CustomAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_products")
public class OrderProductEntity extends CustomAudit implements Serializable {
    private Long id;
    private String name;
    private Double price;
    private Long quantity;
    private Long orderId;
}
