package com.dart.product.dto_model.product_feedback;



import com.dart.product.entity.product_feedback_entity.ProductFeedBackCacheEntity;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchAllProductFeedBackModel {

  private Boolean status;
  private String message;
  private List<ProductFeedBackCacheEntity> productFeedBack;

}
