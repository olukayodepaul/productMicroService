package com.dart.product.dto_model.product_feedback;



import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductFeedBackReqDTO {

    private String feedback_type;

    //not part of the request
    private Integer product_id;
    private String user_id;
    private Integer id;
    private UUID organisation_id;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;
    private String message;

}
