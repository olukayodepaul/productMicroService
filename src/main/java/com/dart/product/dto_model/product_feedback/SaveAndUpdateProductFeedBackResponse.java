package com.dart.product.dto_model.product_feedback;


import com.dart.product.entity.product_feedback_entity.ProductFeedBackDbEntity;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAndUpdateProductFeedBackResponse {
    private Boolean status;
    private String error;
    private ProductFeedBackDbEntity productFeedback;
}
