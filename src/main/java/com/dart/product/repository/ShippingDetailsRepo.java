package com.dart.product.repository;


import com.dart.product.entity.shipping_details_model.ShippingDetailsDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ShippingDetailsRepo extends JpaRepository<ShippingDetailsDbModel,Long> {
    Optional<ShippingDetailsDbModel> findByIdAndOrganisationIdAndIsActive(Integer id, UUID organisation_id, boolean is_active);
}
