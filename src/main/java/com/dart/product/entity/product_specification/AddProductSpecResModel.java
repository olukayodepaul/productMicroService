package com.dart.product.entity.product_specification;



import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductSpecResModel {

    private Integer product_id;
    private double weight;
    private String material_description;
    private String certification_description;
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

    //extract data not part of the request data
    private UUID organisation_id;
    private LocalDateTime created_at;

}
