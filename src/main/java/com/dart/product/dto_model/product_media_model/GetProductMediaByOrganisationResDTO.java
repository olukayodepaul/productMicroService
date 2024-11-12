package com.dart.product.dto_model.product_media_model;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductMediaByOrganisationResDTO {
    private boolean status;
    private String message;
    private List<ProductMedia> product_media;
    private PaginationMetadata pagination; // Pagination applies here, not on media types.

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductMedia {
        private Integer product_id;
        private Boolean is_active;
        private List<ImageMedia> image_media_type;
        private List<VideoMedia> video_media_type;

        @Builder
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class VideoMedia {
            private Integer id;
            private Boolean is_primary;
            private String media_type;
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
            private String media_type;
            private String media_url;
            private LocalDateTime updated_at;
            private LocalDateTime created_at;
        }
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaginationMetadata {
        private int currentPage;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private Integer previousOffset;
        private Integer nextOffset;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
    }
}
