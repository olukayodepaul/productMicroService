package com.dart.product.entity.special_offers_model;


import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchAllSpecialOfferModel {

    private Boolean status;
    private String message;
    private List<SpecialOffersCacheModel> specialOffer;

}
