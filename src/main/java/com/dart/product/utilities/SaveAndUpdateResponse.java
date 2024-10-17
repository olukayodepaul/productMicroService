package com.dart.product.utilities;

import com.dart.product.entity.product_model.ProductDbModel;
import lombok.*;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAndUpdateResponse {
    private Boolean status;
    private String error;
    private ProductDbModel product;
}
