package com.dart.product.dto_model.product_tags_model;


import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAndUpdateProductTagResponse {
    private Boolean status;
    private String error;
    private ProductTagDbModel productTags;
}
