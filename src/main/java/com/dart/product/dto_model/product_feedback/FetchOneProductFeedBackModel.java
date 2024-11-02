package com.dart.product.dto_model.product_feedback;



import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchOneProductFeedBackModel {

  private Boolean status;
  private String message;
  private ProductFeedBackCacheModel productFeedBack;

}
