package com.dart.product.repository;


import com.dart.product.entity.product_tags_model.ProductTagDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTagRepo extends JpaRepository<ProductTagDbModel, Long> {
}
