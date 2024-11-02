package com.dart.product.dto_model.product_reviews_model;



import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchAllProductReviewModel {

    private Boolean status;
    private String message;
    private List<ProductReviewCacheModel> productReview;

}
