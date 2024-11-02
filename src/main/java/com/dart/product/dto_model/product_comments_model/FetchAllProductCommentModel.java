package com.dart.product.dto_model.product_comments_model;



import com.dart.product.entity.product_comment_entity.ProductCommentCacheModel;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchAllProductCommentModel {

    private Boolean status;
    private String message;
    private List<ProductCommentCacheModel> productComment;

}
