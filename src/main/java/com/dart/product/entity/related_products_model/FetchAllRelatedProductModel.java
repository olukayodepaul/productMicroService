package com.dart.product.entity.related_products_model;


import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchAllRelatedProductModel {

    private Boolean status;
    private String message;
    private List<RelatedProductsCacheModel> relatedProduct;

}
