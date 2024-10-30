package com.dart.product.entity.product_comments_model;


import com.dart.product.entity.product_tags_model.ProductTagDbModel;
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
