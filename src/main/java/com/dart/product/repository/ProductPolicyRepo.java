package com.dart.product.repository;

import com.dart.product.entity.product_policy_model.ProductPolicyDbModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ProductPolicyRepo extends JpaRepository<ProductPolicyDbModel, Long> {
    Optional<ProductPolicyDbModel> findByIdAndOrganisationIdAndIsActiveAndProductId(Integer id, UUID organisation_id, boolean is_active, Integer product_id);
    Optional<List<ProductPolicyDbModel>> findByOrganisationIdAndIsActiveAndProductId(UUID organisation_id, boolean is_active, Integer product_id);
    Optional<ProductPolicyDbModel> findByIsActiveAndProductIdAndOrganisationId(boolean is_active, Integer product_id, UUID organisation_id);
}
