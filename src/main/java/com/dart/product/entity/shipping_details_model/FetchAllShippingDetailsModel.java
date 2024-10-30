package com.dart.product.entity.shipping_details_model;



import lombok.*;
import java.util.List;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchAllShippingDetailsModel {
    private Boolean status;
    private String message;
    private List<ShippingDetailsCacheModel> shippingDetails;
}
