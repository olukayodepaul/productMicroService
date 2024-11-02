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
public class ProductFeedBackOneResModel {

    private boolean status;
    private String message;
    private ProductFeedBack product_feedback;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductFeedBack {

        private Integer id;
        private Integer product_id;
        private UUID organisation_id;

        private UUID user_id;
        private String feedback_type;

        private boolean is_active;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;

    }
}
