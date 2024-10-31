package com.dart.product.repository;


import com.dart.product.entity.product_feedback.ProductFeedBackDbModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductFeedBackRepo extends JpaRepository<ProductFeedBackDbModel,Long> {
    Optional<ProductFeedBackDbModel> findByIdAndProductIdAndOrganisationIdAndIsActive(Integer id, Integer product_id, UUID organisation_id, boolean is_active);
    Optional<List<ProductFeedBackDbModel>> findByProductIdAndOrganisationIdAndIsActive(Integer product_id, UUID organisation_id, boolean is_active);
}
