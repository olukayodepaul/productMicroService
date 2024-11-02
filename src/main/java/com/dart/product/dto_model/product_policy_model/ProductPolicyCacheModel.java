package com.dart.product.dto_model.product_policy_model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@RedisHash("product_policies")
public class ProductPolicyCacheModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productId;
    private UUID organisationId;

    private String warrantyDescription;
    private String warrantyPeriod;
    private String returnPolicyDescription;

    private boolean isActive;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

}

