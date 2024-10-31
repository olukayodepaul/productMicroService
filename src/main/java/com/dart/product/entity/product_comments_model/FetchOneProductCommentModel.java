package com.dart.product.entity.product_comments_model;



import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchOneProductCommentModel {

    private Boolean status;
    private String message;
    private ProductCommentCacheModel productComment;

}
