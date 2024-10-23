package com.dart.product.entity.product_media_model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrimaryProductResModel {

    private boolean status;
    private String message;
    private Integer product_id;
    private String media_type;
    private Boolean isActive;
    private IsTrue previous_primary_media;
    private IsFalse current_primary_media;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IsTrue {
        private Integer id;

        private Boolean is_primary;
        private String media_url;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IsFalse {
        private Integer id;

        private Boolean is_primary;
        private String media_url;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;
    }
}