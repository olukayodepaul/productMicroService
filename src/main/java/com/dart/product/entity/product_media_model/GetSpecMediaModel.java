package com.dart.product.entity.product_media_model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetSpecMediaModel {

    private boolean status;
    private String message;
    private Integer product_id;
    private Boolean is_active;
    private String media_type;
    private List<Media> product_media ;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Media {
        private Integer id;

        private Boolean is_primary;
        private String media_url;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;
    }
}



