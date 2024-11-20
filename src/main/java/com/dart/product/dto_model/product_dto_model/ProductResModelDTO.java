package com.dart.product.dto_model.product_dto_model;

import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResModelDTO {

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
        private String currency;
        private Double discount;
        private Integer category_id;
        private Integer brand_id;
        private Boolean is_active;
        private Integer return_policy_id;
        private Integer warranty_policy_id;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;
    }

}

