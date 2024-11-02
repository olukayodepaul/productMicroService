package com.dart.product.dto_model.product_policy_model;



import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductPolicyReqModel {

    private Integer product_id;
    private String warranty_description;
    private String warranty_period;
    private String return_policy_description;


    //not part of the request
    private Integer id;
    private UUID organisation_id;
    private boolean is_active;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;
    private String message;

}
