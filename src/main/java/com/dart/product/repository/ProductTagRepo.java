package com.dart.product.repository;


import com.dart.product.dto_model.product_tags_model.ProductTagDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductTagRepo extends JpaRepository<ProductTagDbModel, Long> {
    Optional<ProductTagDbModel> findByIdAndProductIdAndOrganisationIdAndIsActive(Integer id, Integer product_id, UUID organisation_id, boolean is_active);
    Optional<List<ProductTagDbModel>> findByProductIdAndOrganisationIdAndIsActive(Integer product_id, UUID organisation_id, boolean is_active);
}
