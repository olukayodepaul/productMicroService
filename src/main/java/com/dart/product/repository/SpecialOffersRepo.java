package com.dart.product.repository;


import com.dart.product.dto_model.special_offers_model.SpecialOffersDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpecialOffersRepo extends JpaRepository<SpecialOffersDbModel, Long> {
    Optional<SpecialOffersDbModel> findByIdAndProductIdAndOrganisationIdAndIsActive(Integer id, Integer product_id, UUID organisation_id, boolean is_active);
    Optional<List<SpecialOffersDbModel>> findByProductIdAndOrganisationIdAndIsActive(Integer product_id, UUID organisation_id, boolean is_active);
}
