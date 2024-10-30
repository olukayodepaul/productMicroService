package com.dart.product.repository;

import com.dart.product.entity.related_products_model.RelatedProductsDbModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RelatedProductsRepo extends JpaRepository<RelatedProductsDbModel,Long> {
}
