package com.dart.product.dto_model.special_offers_model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@RedisHash("special_offers")
public class SpecialOffersCacheModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productId;
    private UUID organisationId;


    //add items here.....
    private String offerDescription;
    private double discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


    private boolean isActive;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

}

