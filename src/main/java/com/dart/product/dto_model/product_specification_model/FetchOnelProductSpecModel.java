package com.dart.product.dto_model.product_specification_model;



import lombok.*;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchOnelProductSpecModel {
    private Boolean status;
    private String message;
    private ProductSpecificationCacheModel productSpec;
}
