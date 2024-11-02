package com.dart.product.dto_model.product_specification_model;


import lombok.*;
import java.time.LocalDateTime;



@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductSpecResModel {

    private boolean status;
    private String message;
    private Integer id;
    private Integer product_id;
    private double weight;
    private Dimensions dimensions;
    private String material_description;
    private String certification_description;


    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Dimensions {
        private double length;
        private double width;
        private double height;
    }

    private LocalDateTime updated_at;
    private LocalDateTime created_at;

}

