package com.dart.product.repository;



import com.dart.product.dto_model.shipping_details_model.ShippingDetailsDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ShippingDetailsRepo extends JpaRepository<ShippingDetailsDbModel,Long> {
    Optional<ShippingDetailsDbModel> findByIdAndOrganisationIdAndProductIdAndIsActive(Integer id, UUID organisation_id, Integer product_id, boolean is_active);
    Optional<List<ShippingDetailsDbModel>> findByOrganisationIdAndProductIdAndIsActive(UUID organisation_id, Integer product_id,  boolean is_active);
}
