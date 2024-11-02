package com.dart.product.repository;

import com.dart.product.entity.product_entity.ProductDbEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductsRepo  extends JpaRepository<ProductDbEntity, Long> {
    Optional<List<ProductDbEntity>> findByOrganisationIdAndIsActive (UUID organisation_id, boolean is_active, Sort sort);
    Optional<ProductDbEntity> findByIdAndOrganisationIdAndIsActive(Integer id, UUID organisation_id, boolean is_active);
}