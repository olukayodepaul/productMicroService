package com.dart.product.dto_model.product_feedback;



import com.dart.product.entity.product_feedback_entity.ProductFeedBackCacheEntity;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchProductFeedBackModel {

  private Boolean status;
  private String message;
  private ProductFeedBackCacheEntity productFeedBack;

}
