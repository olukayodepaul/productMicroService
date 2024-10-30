package com.dart.product.entity.product_policy_model;


import lombok.*;
import java.util.List;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchAllProductPolicyModel {
    private Boolean status;
    private String message;
    private List<ProductPolicyCacheModel> productPolicy;
}
