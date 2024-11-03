package com.dart.product.dto_model.product_dto_model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllProductResDTO {

    private boolean status;
    private String message;
    private List<Product> products;
    private PaginationMetadata pagination;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product {
        private Integer id;
        private String name;
        private String description;
        private Double price;
        private Double discount;
        private Integer category_id;
        private Integer brand_id;
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
