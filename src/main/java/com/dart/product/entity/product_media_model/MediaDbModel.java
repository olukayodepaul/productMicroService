package com.dart.product.entity.product_media_model;

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
@Table(name = "product_media")
public class MediaDbModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;
    @Column(name = "organisation_id")
    private UUID organisationId;
    @Column(name = "media_type")
    private String mediaType;
    @Column(name = "media_url")
    private String mediaUrl;
    @Column(name = "is_primary")
    private Boolean isPrimary;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}