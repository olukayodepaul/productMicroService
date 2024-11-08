package com.dart.product.dto_model.product_media_model;

import com.dart.product.entity.prodct_media.ProductMediaCacheEntity;
import lombok.*;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchProductMediaModel {
    private Boolean status;
    private String message;
    private ProductMediaCacheEntity productMedia;
}
