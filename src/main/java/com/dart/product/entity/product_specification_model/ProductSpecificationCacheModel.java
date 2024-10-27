package com.dart.product.entity.product_specification_model;



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
@RedisHash("products")
public class ProductSpecificationCacheModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productId;
    private UUID organisationId;
    private double length;
    private double width;
    private double height;
    private double weight;
    private String material_description;
    private String certification_description;
    private boolean is_active;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;

}