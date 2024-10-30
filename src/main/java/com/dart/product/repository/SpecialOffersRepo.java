package com.dart.product.repository;


import com.dart.product.entity.special_offers_model.SpecialOffersDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialOffersRepo extends JpaRepository<SpecialOffersDbModel, Long> {
}
