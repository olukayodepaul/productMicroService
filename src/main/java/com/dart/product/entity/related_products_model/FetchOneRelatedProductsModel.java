package com.dart.product.entity.related_products_model;


import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchOneRelatedProductsModel {

    private Boolean status;
    private String message;
    private RelatedProductsCacheModel relatedProducts;

}
