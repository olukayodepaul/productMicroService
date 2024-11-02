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
public class AddProductCommentReqlDTO {


    private String comment_text;

    //not part of the request
    private String user_id;
    private Integer product_id;
    private Integer id;
    private UUID organisation_id;
    private boolean is_active;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;
    private String message;

}
