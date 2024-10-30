package com.dart.product.repository;


import com.dart.product.entity.product_feedback.ProductFeedBackDbModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFeedBackRepo extends JpaRepository<ProductFeedBackDbModel,Long> {
}
