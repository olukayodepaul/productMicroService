package com.dart.product.dto_model.special_offers_model;


import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchOneSpecialOfferModel {

    private Boolean status;
    private String message;
    private SpecialOffersCacheModel specialOffer;

}
