package com.dart.product.repository;

import com.dart.product.entity.product_comment_entity.ProductCommentDbEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductCommentRepo extends JpaRepository<ProductCommentDbEntity,Long> {
    Optional<ProductCommentDbEntity> findByIdAndProductIdAndOrganisationIdAndIsActive(Integer id, Integer product_id, UUID organisation_id, boolean is_active);
    Optional<Page<ProductCommentDbEntity>> findByProductIdAndOrganisationIdAndIsActive(Integer product_id, UUID organisation_id, boolean is_active, Pageable pageable);
}
