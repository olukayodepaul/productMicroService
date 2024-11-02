package com.dart.product.dto_model.special_offers_model;


import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAndUpdateSpecialOffersResponse {
    private Boolean status;
    private String error;
    private SpecialOffersDbModel specialOffers;
}
