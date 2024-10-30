package com.dart.product.repository;


import com.dart.product.entity.product_specification_model.ProductSpecificationDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductSpecificationRepo extends JpaRepository<ProductSpecificationDbModel, Long> {
    Optional<ProductSpecificationDbModel> findByIdAndOrganisationIdAndIsActiveAndProductId(Integer id, UUID organisation_id, boolean is_active, Integer product_id);
    Optional<List<ProductSpecificationDbModel>> findByOrganisationIdAndIsActiveAndProductId(UUID organisation_id, boolean is_active, Integer product_id);
    Optional<ProductSpecificationDbModel> findIsActiveAndProductIdAndByOrganisationId(boolean is_active, Integer product_id, UUID organisation_id);
}
