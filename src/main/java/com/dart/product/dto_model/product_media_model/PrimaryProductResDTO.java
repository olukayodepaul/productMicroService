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
public class PrimaryProductResDTO {

    private boolean status;
    private String message;
    private Integer product_id;
    private String media_type;
    private Boolean is_active;
    private UUID organisation_id;
    private CurrentPrimaryMedia current_primary_media;
    private PreviousPrimaryMedia previous_primary_media;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CurrentPrimaryMedia {
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
    public static class PreviousPrimaryMedia {
        private Integer id;

        private Boolean is_primary;
        private String media_url;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;
    }



}