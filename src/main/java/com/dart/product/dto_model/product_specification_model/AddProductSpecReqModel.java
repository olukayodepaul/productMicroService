package com.dart.product.dto_model.product_specification_model;



import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductSpecReqModel {

    private Integer product_id;  //not optional create, optional for update
    private String weight;
    private String material_description;
    private String certification_description;
    private Dimensions dimensions;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Dimensions {
        private String length;
        private String width;
        private String height;
    }

    //extract data not part of the request data
    private Integer id;
    private UUID organisation_id;
    private boolean is_active;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;
    private String message;

}
