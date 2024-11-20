package com.dart.product.repository;

import com.dart.product.entity.product_policy_warranty_entity.ProductPolicyDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductPolicyRepo extends JpaRepository<ProductPolicyDbEntity, Long> {
    Optional<ProductPolicyDbEntity> findByIdAndOrganisationIdAndIsActiveAndProductId(Integer id, UUID organisation_id, boolean is_active, Integer product_id);
    Optional<List<ProductPolicyDbEntity>> findByOrganisationIdAndIsActiveAndProductId(UUID organisation_id, boolean is_active, Integer product_id);
    Optional<ProductPolicyDbEntity> findByIsActiveAndProductIdAndOrganisationId(boolean is_active, Integer product_id, UUID organisation_id);
}
