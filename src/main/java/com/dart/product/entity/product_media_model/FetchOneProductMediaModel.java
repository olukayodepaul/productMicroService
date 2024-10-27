package com.dart.product.entity.product_media_model;

import lombok.*;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchOneProductMediaModel {
    private Boolean status;
    private String message;
    private ProductMediaCacheModel productMedia;
}
