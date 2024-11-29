package com.reactive.order;

import com.reactive.config.audit.CustomAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderEntity extends CustomAudit implements Serializable {
    @Id
    private Long id;
    private String fiscalCode;
    private OrderStatus status;
    private Double price;
    private String currency;
    private Long customerId;
}
