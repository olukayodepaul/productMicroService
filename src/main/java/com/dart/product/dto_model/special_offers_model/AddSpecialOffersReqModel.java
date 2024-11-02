package com.dart.product.dto_model.special_offers_model;



import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddSpecialOffersReqModel {

    private Integer product_id;

    private String offer_description;
    private double discount_percentage;
    private LocalDateTime start_date;
    private LocalDateTime end_date;


    //not part of the request
    private Integer id;
    private UUID organisation_id;
    private boolean is_active;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;
    private String message;

}
