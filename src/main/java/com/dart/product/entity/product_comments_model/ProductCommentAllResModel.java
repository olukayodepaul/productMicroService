package com.dart.product.entity.product_comments_model;




import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCommentAllResModel {

    private boolean status;
    private String message;
    private List<ProductComment> product_comments;

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

        private boolean is_active;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;

    }
}
