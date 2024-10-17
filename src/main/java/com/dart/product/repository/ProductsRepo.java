package com.dart.product.repository;

import com.dart.product.entity.product_model.ProductResModel;
import com.dart.product.entity.product_model.ProductDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductsRepo  extends JpaRepository<ProductDbModel, Long> {
    Optional<ProductDbModel> findById(Integer id);
    Optional<List<ProductDbModel>> findByOrganisationIdAndIsActive (UUID organisation_id, boolean is_active);
    Optional<ProductDbModel> findByIdAndOrganisationIdAndIsActive(Integer id,  UUID organisation_id, boolean is_active);
}


