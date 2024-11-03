package com.dart.product.dto_model.product_comments_model;



import com.dart.product.entity.product_comment_entity.ProductCommentCacheModel;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchProductCommentModel {

    private Boolean status;
    private String message;
    private ProductCommentCacheModel productComment;

}
