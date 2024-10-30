package com.dart.product.entity.product_tags_model;




import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTagOneResModel {

    private boolean status;
    private String message;
    private SpecialOffers product_tags;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SpecialOffers {

        private Integer id;
        private Integer product_id;
        private UUID organisation_id;

        private String tag;

        private boolean is_active;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;

    }
}
