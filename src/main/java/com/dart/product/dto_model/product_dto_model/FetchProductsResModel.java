package com.dart.product.dto_model.product_dto_model;

import com.dart.product.entity.product_entity.ProductCacheEntity;
import lombok.*;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchProductsResModel {
    private Boolean status;
    private String message;
    private ProductCacheEntity products;
}

