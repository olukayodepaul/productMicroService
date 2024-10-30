package com.dart.product.entity.product_policy_model;


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
    private ProductPolicyCacheModel productPolicy;

}
