package com.dart.product.repository;


import com.dart.product.entity.product_comment_entity.ProductCommentDbEntity;
import com.dart.product.entity.product_feedback_entity.ProductFeedBackDbEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductFeedBackRepo extends JpaRepository<ProductFeedBackDbEntity,Long> {
    Optional<ProductFeedBackDbEntity> findByProductIdAndOrganisationIdAndUserId(Integer product_id, UUID organisation_id, UUID user_id);
    Optional<Page<ProductFeedBackDbEntity>> findByOrganisationId(UUID organisation_id, Pageable pageable);
}
