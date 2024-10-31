package com.dart.product.entity.product_reviews_model;



import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchOneProductReviewModel {

    private Boolean status;
    private String message;
    private ProductReviewCacheModel productReview;

}
