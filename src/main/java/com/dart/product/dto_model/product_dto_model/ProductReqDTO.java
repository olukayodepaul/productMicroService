package com.dart.product.dto_model.product_dto_model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReqDTO {

    private String name;
    private String description;
    private Double price;
    private Double discount;

    //this is coming from productCategoryMicroService
    private String category_id;

    //additional information
    private String brand_id;
    private String  return_policy_id;
    private String warranty_policy_id;
    private String currency;

    //not part of the request
    private Integer id;
    private  UUID organisation_id;
    private UUID created_by ;
    private boolean is_active;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;


}

