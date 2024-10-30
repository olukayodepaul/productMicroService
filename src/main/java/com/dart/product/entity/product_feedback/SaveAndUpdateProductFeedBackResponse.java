package com.dart.product.entity.product_feedback;


import com.dart.product.entity.product_comments_model.ProductCommentDbModel;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAndUpdateProductFeedBackResponse {
    private Boolean status;
    private String error;
    private ProductFeedBackDbModel productFeedback;
}
