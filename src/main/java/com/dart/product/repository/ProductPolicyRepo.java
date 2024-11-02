package com.dart.product.repository;

import com.dart.product.dto_model.product_policy_model.ProductPolicyDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductPolicyRepo extends JpaRepository<ProductPolicyDbModel, Long> {
    Optional<ProductPolicyDbModel> findByIdAndOrganisationIdAndIsActiveAndProductId(Integer id, UUID organisation_id, boolean is_active, Integer product_id);
    Optional<List<ProductPolicyDbModel>> findByOrganisationIdAndIsActiveAndProductId(UUID organisation_id, boolean is_active, Integer product_id);
    Optional<ProductPolicyDbModel> findByIsActiveAndProductIdAndOrganisationId(boolean is_active, Integer product_id, UUID organisation_id);
}
