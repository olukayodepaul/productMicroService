package com.dart.product.entity.product_media_model;

import com.dart.product.entity.product_model.ProductCacheModel;
import lombok.*;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchIndividualMediaProductModel {
    private Boolean status;
    private String message;
    private ProductMediaCacheModel productMedia;
}
