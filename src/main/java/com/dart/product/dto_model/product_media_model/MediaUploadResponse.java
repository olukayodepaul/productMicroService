package com.dart.product.dto_model.product_media_model;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaUploadResponse {
    private String extension;
    private String fileName;
    private String mediaType;
}
