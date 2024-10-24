package com.dart.product.repository;


import com.dart.product.entity.product_specification.ProductSpecificationDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSpecificationRepo extends JpaRepository<ProductSpecificationDbModel, Long> {

}
