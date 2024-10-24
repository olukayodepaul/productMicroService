package com.dart.product.entity.product_specification;

import com.dart.product.entity.product_media_model.MediaDbModel;
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
