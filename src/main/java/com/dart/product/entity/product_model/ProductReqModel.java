package com.dart.product.entity.product_model;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReqModel {
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private Integer category_id;
    private  UUID organisation_id;
}

