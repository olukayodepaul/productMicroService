package com.dart.product.entity.product_entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products_log_trail")
public class ProductDbTrailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer products_id;
    private String change_type;

    @Column(name = "organisation_id")
    private UUID organisationId;

    private UUID created_updated_deleted_by ;
    private String name;
    private String description;
    private Double price;
    private String currency;
    private Double discount;
    private Integer category_id;
    private Integer brand_id;
    private Integer return_policy_id;
    private Integer warranty_policy_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private UUID consolidated;
    private String old_new_change;

    @Column(name = "is_active")
    private Boolean isActive;
}
