package com.dart.product.dto_model.product_policy_model;


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
@Table(name = "product_policies")
public class ProductPolicyDbModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "organisation_id")
    private UUID organisationId;

    @Column(name = "warranty_description")
    private String warrantyDescription;

    @Column(name = "warranty_period")
    private String warrantyPeriod;

    @Column(name = "return_policy_description")
    private String returnPolicyDescription;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}

