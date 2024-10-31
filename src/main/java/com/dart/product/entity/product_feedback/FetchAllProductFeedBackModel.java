package com.dart.product.entity.product_feedback;



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
  private List<ProductFeedBackCacheModel> productFeedBack;

}
