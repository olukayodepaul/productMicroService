package com.dart.product.dto_model.product_media_model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductMediaResDTO {

    private boolean status;
    private String message;
    private ProductMedia data;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductMedia {
        private Integer id;

        private Integer product_id;
        private UUID organisation_id;
        private UUID created_by ;
        private String media_type;
        private Boolean is_primary;
        private Boolean is_active;
        private String media_url;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;
    }

}