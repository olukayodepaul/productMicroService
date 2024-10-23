package com.dart.product.entity.product_media_model;

import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductMediaResModel {

    private boolean status;
    private String message;
    private ProductMedia Product_media;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductMedia {
        private Integer id;

        private Integer product_id;
        private String media_type;
        private Boolean is_primary;
        private Boolean isActive;
        private String media_url;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;
    }

}