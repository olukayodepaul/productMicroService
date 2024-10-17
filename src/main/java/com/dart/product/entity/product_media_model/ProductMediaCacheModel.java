package com.dart.product.entity.product_media_model;

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
@RedisHash("product_media")
public class ProductMediaCacheModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer product_id;
    private UUID organisation_id;
    private String media_type;
    private String media_url;
    private Boolean is_primary;
    private Boolean isActive;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;


}
