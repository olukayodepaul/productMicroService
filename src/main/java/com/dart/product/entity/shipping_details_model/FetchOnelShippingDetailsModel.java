package com.dart.product.entity.shipping_details_model;


import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchOnelShippingDetailsModel {

    private Boolean status;
    private String message;
    private ShippingDetailsCacheModel shippingDetails;
}
