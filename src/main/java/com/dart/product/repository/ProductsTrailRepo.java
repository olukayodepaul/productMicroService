package com.dart.product.repository;


import com.dart.product.entity.product_entity.ProductDbTrailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductsTrailRepo extends JpaRepository<ProductDbTrailEntity, Long> {}