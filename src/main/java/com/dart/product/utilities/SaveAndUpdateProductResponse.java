package com.dart.product.utilities;

import com.dart.product.entity.product_entity.ProductDbEntity;
import lombok.*;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAndUpdateProductResponse {
    private Boolean status;
    private String error;
    private ProductDbEntity product;
}
