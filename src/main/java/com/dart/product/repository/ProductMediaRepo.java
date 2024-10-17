package com.dart.product.repository;

import com.dart.product.entity.product_media_model.MediaDbModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMediaRepo extends JpaRepository<MediaDbModel, Long> {}
