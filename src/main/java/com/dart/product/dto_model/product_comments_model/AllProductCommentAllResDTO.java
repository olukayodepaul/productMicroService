package com.dart.product.dto_model.product_comments_model;




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
public class AllProductCommentAllResDTO {

    private boolean status;
    private String message;
    private List<ProductComment> product_comments;
    private PaginationMetadata pagination;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductComment {

        private Integer id;
        private Integer product_id;
        private UUID organisation_id;
        private String comment_text;
        private UUID user_id;
        private Boolean is_active;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaginationMetadata {
        private int currentPage;       // Current page number (1-based)
        private int pageSize;          // Number of items per page
        private long totalElements;     // Total number of items
        private int totalPages;         // Total number of pages
        private Integer previousOffset; // Offset for the previous page
        private Integer nextOffset;     // Offset for the next page
        private boolean hasPreviousPage; // Indicates if there is a previous page
        private boolean hasNextPage;     // Indicates if there is a next page
    }
}
