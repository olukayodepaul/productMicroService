package com.dart.product.dto_model.product_media_model;

import com.dart.product.entity.prodct_media.MediaDbEntity;
import lombok.*;



@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAndUpdateMediaResponse {
    private Boolean status;
    private String error;
    private MediaDbEntity productMedia;
}
