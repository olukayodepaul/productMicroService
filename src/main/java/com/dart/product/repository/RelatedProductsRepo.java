package com.dart.product.repository;

import com.dart.product.entity.related_products_model.RelatedProductsDbModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface RelatedProductsRepo extends JpaRepository<RelatedProductsDbModel,Long> {
    Optional<RelatedProductsDbModel> findByIdAndProductIdAndOrganisationIdAndIsActive(Integer id, Integer product_id, UUID organisation_id, boolean is_active);
    Optional<List<RelatedProductsDbModel>> findByProductIdAndOrganisationIdAndIsActive(Integer product_id, UUID organisation_id, boolean is_active);
}
