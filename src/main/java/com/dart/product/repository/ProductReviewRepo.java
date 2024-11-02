package com.dart.product.repository;

import com.dart.product.dto_model.product_reviews_model.ProductReviewDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductReviewRepo extends JpaRepository<ProductReviewDbModel, Long> {
    Optional<ProductReviewDbModel> findByIdAndProductIdAndOrganisationIdAndIsActive(Integer id, Integer product_id, UUID organisation_id, boolean is_active);
    Optional<List<ProductReviewDbModel>> findByProductIdAndOrganisationIdAndIsActive(Integer product_id, UUID organisation_id, boolean is_active);
}
