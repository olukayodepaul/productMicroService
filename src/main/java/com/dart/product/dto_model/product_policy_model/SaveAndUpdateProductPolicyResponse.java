package com.dart.product.dto_model.product_policy_model;



import com.dart.product.entity.product_policy_warranty_entity.ProductPolicyDbEntity;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAndUpdateProductPolicyResponse {
    private Boolean status;
    private String error;
    private ProductPolicyDbEntity productPolicy;
}
