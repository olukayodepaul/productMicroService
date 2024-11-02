package com.dart.product.dto_model.related_products_model;




import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatedProductsOneResModel {

    private boolean status;
    private String message;
    private RelatedProduct related_products;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RelatedProduct {

        private Integer id;
        private Integer product_id;
        private UUID organisation_id;

        private Integer related_product_id;

        private boolean is_active;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;

    }

}
