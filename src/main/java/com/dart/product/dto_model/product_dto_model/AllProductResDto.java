package com.dart.product.dto_model.product_dto_model;



import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllProductResDto {

    private boolean status;
    private String message;
    private List<Product> product;

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

}
