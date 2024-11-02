package com.dart.product.repository;

import com.dart.product.dto_model.related_products_model.RelatedProductsDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RelatedProductsRepo extends JpaRepository<RelatedProductsDbModel,Long> {
    Optional<RelatedProductsDbModel> findByIdAndProductIdAndOrganisationIdAndIsActive(Integer id, Integer product_id, UUID organisation_id, boolean is_active);
    Optional<List<RelatedProductsDbModel>> findByProductIdAndOrganisationIdAndIsActive(Integer product_id, UUID organisation_id, boolean is_active);
}
