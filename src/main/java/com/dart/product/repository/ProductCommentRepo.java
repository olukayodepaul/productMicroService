package com.dart.product.repository;

import com.dart.product.entity.product_comments_model.ProductCommentDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ProductCommentRepo extends JpaRepository<ProductCommentDbModel,Long> {
    Optional<ProductCommentDbModel> findByIdAndProductIdAndOrganisationIdAndIsActive(Integer id, Integer product_id, UUID organisation_id, boolean is_active);
    Optional<List<ProductCommentDbModel>> findByProductIdAndOrganisationIdAndIsActive(Integer product_id, UUID organisation_id, boolean is_active);
}
