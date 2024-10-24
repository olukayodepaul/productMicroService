package com.dart.product.entity.product_specification;

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
@Table(name = "product_specifications")
public class ProductSpecificationDbModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "organisation_id")
    private UUID organisationId;

    private double length;
    private double width;
    private double height;
    private double weight;

    @Column(name = "material_description")
    private String materialDescription;

    @Column(name = "certification_description")
    private String certificationDescription;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}


