package com.dart.product.entity.product_model;

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
public class ProductCacheModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private UUID organisation_id;
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private Integer category_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Boolean is_active;
}
