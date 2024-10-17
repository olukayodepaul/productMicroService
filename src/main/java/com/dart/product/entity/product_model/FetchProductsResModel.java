package com.dart.product.entity.product_model;

import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchProductsResModel {
    private Boolean status;
    private String message;
    private ProductCacheModel products;
}

