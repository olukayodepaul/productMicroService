package com.dart.product.repository;

import com.dart.product.entity.product_comments_model.ProductCommentDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface ProductCommentRepo extends JpaRepository<ProductCommentDbModel,Long> {
}
