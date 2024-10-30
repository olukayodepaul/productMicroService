package com.dart.product.entity.product_reviews_model;



import com.dart.product.entity.product_policy_model.ProductPolicyDbModel;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAndUpdateProductReviewResponse {
    private Boolean status;
    private String error;
    private ProductReviewDbModel productReviews;
}
