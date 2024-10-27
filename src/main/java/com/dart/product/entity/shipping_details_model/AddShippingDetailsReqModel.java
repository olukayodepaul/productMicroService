package com.dart.product.entity.shipping_details_model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddShippingDetailsReqModel {

    private Integer product_id; //optional for update

    private String shipping_method;
    private double shipping_cost;
    private String estimated_delivery_time;
    private String country_code;
    private String region;
    private double customs_fees;
    private String handling_time;
    private String cross_border;

    //not part of the request
    private Integer id;
    private UUID organisation_id;
    private boolean is_active;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;
    private String message;

}
