package com.dart.product.dto_model.product_comments_model;


import com.dart.product.entity.product_comment_entity.ProductCommentDbModel;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAndUpdateProductCommentResponse {
    private Boolean status;
    private String error;
    private ProductCommentDbModel productComments;
}
