package com.dart.product.dto_model.product_comments_model;




import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCommentResDTO {

    private boolean status;
    private String message;
    private ProductComment product_comments;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductComment {

        private Integer id;
        private Integer product_id;
        private UUID organisation_id;

        private String comment_text;
        private UUID user_id;

        private Boolean is_active;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;

    }
}
