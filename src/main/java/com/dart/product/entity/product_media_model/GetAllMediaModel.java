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
public class GetAllMediaModel {

    private boolean status;
    private String message;
    private Integer product_id;
    private Boolean isActive;
    private List<ImageMedia> image_media_type ;
    private List<VideoMedia> video_media_type;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VideoMedia {
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
    public static class ImageMedia {
        private Integer id;

        private Boolean is_primary;
        private String media_url;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;
    }

}



