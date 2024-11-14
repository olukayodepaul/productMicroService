package com.dart.product.dto_model.product_media_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaUploadReqModel {

    @JsonProperty("is_primary")
    private boolean isPrimary;

    //not part of the request

    private Integer id;
    private Integer product_media_id;
    private UUID organisation_id;
    private Integer product_id;
    private String media_type;
    private Boolean is_primary;
    private Boolean isActive;
    private String media_url;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;

}
