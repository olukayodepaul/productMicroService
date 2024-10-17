package com.dart.product.entity.product_media_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaUploadReqModel {

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("is_primary")
    private boolean isPrimary;

}
