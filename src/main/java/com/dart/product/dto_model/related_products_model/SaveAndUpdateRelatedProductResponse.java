package com.dart.product.dto_model.related_products_model;



import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAndUpdateRelatedProductResponse {
    private Boolean status;
    private String error;
    private RelatedProductsDbModel relatedProducts;
}
