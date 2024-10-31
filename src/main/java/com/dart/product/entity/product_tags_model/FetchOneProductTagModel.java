package com.dart.product.entity.product_tags_model;


import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchOneProductTagModel {

    private Boolean status;
    private String message;
    private ProductTagCacheModel productTag;

}
