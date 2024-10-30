package com.dart.product.entity.product_policy_model;



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
    private ProductPolicyDbModel productPolicy;
}
