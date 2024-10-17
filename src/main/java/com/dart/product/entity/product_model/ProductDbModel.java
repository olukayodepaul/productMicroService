package com.dart.product.entity.product_model;

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
public class ProductDbModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "organisation_id")
    private UUID organisationId;


    private String name;
    private String description;
    private Double price;
    private Double discount;
    private Integer category_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @Column(name = "is_active")
    private Boolean isActive;

}
