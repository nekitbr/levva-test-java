package com.reactive.config.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomAudit {

    @JsonIgnore
    @CreatedDate
    @Column("created_at")
    private Instant createdAt;

    @JsonIgnore
    @CreatedBy
    @Column("created_by")
    private String createdBy;

    @JsonIgnore
    @LastModifiedDate
    @Column("updated_at")
    private Instant updatedAt;

    @JsonIgnore
    @LastModifiedBy
    @Column("updated_by")
    private String updatedBy;

}
