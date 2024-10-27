package com.dart.product.entity.product_specification_model;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAndUpdateProductSpecResponse {
    private Boolean status;
    private String error;
    private ProductSpecificationDbModel productSpec;
}
