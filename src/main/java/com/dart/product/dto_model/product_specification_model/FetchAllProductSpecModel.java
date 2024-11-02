package com.dart.product.dto_model.product_specification_model;

import lombok.*;

import java.util.List;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchAllProductSpecModel {
    private Boolean status;
    private String message;
    private List<ProductSpecificationCacheModel> productSpec;
}

