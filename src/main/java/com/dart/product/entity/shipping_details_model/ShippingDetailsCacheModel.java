package com.dart.product.entity.shipping_details_model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


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
@RedisHash("shipping_details")
public class ShippingDetailsCacheModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productId;
    private UUID organisationId;
    private String shippingMethod;
    private double shippingCost;
    private String estimatedDeliveryTime;
    private String countryCode;
    private String region;
    private double customsFees;
    private String handlingTime;
    private String crossBorder;
    private boolean isActive;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

}

