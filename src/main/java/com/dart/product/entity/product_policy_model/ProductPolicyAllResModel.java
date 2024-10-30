package com.dart.product.entity.product_policy_model;

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
public class ProductPolicyAllResModel {

    private boolean status;
    private String message;
    private List<ProductPolicy> product_policies;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductPolicy {

        private Integer id;
        private Integer product_id;
        private UUID organisation_id;
        private String warranty_description;
        private String warranty_period;
        private String return_policy_description;
        private boolean is_active;
        private LocalDateTime updated_at;
        private LocalDateTime created_at;

    }
}