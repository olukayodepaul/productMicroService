package com.dart.product.entity.shipping_details_model;


import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAndUpdateShippingDetailsResponse {

    private Boolean status;
    private String error;
    private ShippingDetailsDbModel shippingDetails;
}
