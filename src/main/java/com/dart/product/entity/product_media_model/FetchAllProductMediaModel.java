package com.dart.product.entity.product_media_model;

import com.dart.product.entity.product_model.ProductCacheModel;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchAllProductMediaModel {
    private Boolean status;
    private String message;
    private List<ProductMediaCacheModel> productMedia;
}

