package com.dart.product.dto_model.product_dto_model;

import lombok.*;

import java.time.LocalDateTime;
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
    private Integer category_id; //this is coming from productCategoryMicroService
    private Integer brand_id;


    //not part of the request
    private Integer id;
    private  UUID organisation_id;
    private boolean is_active;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;
}

