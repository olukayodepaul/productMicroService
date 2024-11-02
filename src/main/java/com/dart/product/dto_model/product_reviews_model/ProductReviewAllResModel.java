package com.dart.product.dto_model.product_reviews_model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;



@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewAllResModel {

    private boolean status;
    private String message;
    private List<ProductReview> product_reviews;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductReview {

        private Integer id;
        private Integer product_id;
        private UUID organisation_id;
        private String user_id;
        private Integer rating;
        private String review_text;
        private boolean is_active;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;

    }

}
