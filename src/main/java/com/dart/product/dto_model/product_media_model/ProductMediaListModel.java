package com.dart.product.dto_model.product_media_model;

import com.dart.product.entity.prodct_media.ProductContentMediaCacheEntity;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductMediaListModel {
    private Boolean status;
    private String message;
    private List<ProductContentMediaCacheEntity> productMedia;
}

