package com.dart.product.entity.product_model;

import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResModel {

    private boolean status;
    private String message;
    private Product product;

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
        private Boolean is_active;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;
    }

}

