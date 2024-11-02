package com.dart.product.dto_model.product_reviews_model;



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
