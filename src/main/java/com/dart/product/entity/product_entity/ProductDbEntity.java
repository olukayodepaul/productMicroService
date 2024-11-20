package com.dart.product.entity.product_entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class ProductDbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "organisation_id")
    private UUID organisationId;

    private UUID created_by ;
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

    @Column(name = "is_active")
    private Boolean isActive;

}
