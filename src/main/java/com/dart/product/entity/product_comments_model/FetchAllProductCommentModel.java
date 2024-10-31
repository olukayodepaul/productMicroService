package com.dart.product.entity.product_comments_model;



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
