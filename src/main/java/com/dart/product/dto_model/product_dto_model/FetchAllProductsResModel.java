package com.dart.product.dto_model.product_dto_model;

import com.dart.product.entity.product_entity.ProductCacheEntity;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchAllProductsResModel {
    private Boolean status;
    private String message;
    private List<ProductCacheEntity> product;
}

