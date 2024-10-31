package com.dart.product.entity.related_products_model;




import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatedProductsAllResModel {

    private boolean status;
    private String message;
    private List<RelatedProduct> related_products;

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
