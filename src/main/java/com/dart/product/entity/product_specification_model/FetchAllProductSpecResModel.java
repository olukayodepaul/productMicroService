package com.dart.product.entity.product_specification_model;


import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchAllProductSpecResModel {

    private boolean status;
    private String message;
    private List<ProductsSpecifications> products_specifications;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductsSpecifications {
       private Integer id;
       private Integer product_id;
       private double weight;
       private Dimensions dimensions;

        @Builder
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Dimensions {
            private double length;
            private double width;
            private double height;
        }

       private String material_description;
       private String certification_description;
       private LocalDateTime updated_at;
       private LocalDateTime created_at;
   }

}

