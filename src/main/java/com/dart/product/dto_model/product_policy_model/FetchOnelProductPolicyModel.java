package com.dart.product.dto_model.product_policy_model;


import com.dart.product.entity.product_policy_warranty_entity.ProductPolicyCacheEntity;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchOnelProductPolicyModel {

    private Boolean status;
    private String message;
    private ProductPolicyCacheEntity productPolicy;

}
