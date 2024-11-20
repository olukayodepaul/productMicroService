package com.dart.product.dto_model.product_policy_model;


import com.dart.product.entity.product_policy_warranty_entity.ProductPolicyCacheEntity;
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
    private List<ProductPolicyCacheEntity> productPolicy;
}
