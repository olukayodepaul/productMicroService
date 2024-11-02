package com.dart.product.dto_model.related_products_model;



import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddRelatedProductsReqModel {

    private Integer product_id;
    private Integer related_product_id;



    //not part of the request
    private Integer id;
    private UUID organisation_id;
    private boolean is_active;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;
    private String message;

}
