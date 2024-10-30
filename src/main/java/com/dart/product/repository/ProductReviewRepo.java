package com.dart.product.repository;

import com.dart.product.entity.product_reviews_model.ProductReviewDbModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepo extends JpaRepository<ProductReviewDbModel, Long> {
}
