package com.dart.product.repository;

import com.dart.product.dto_model.product_media_model.MediaDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ProductMediaRepo extends JpaRepository<MediaDbModel, Long> {

    Optional<MediaDbModel> findByProductIdAndOrganisationIdAndMediaTypeAndIsPrimary(
            Integer product_id,
            UUID organisation_id,
            String media_type,
            boolean is_primary
    );

    Optional<MediaDbModel> findByIdAndOrganisationIdAndIsActive(
            Integer id,
            UUID organisation_id,
            boolean is_primary
    );

    Optional<MediaDbModel> findByIdAndOrganisationIdAndIsActive(
            Integer id,
            UUID organisation_id,
            Boolean is_active
    );

    Optional<MediaDbModel> findByProductIdAndIsPrimaryAndOrganisationIdAndMediaType(
            Integer product_id,
            boolean is_primary,
            UUID organisation_id,
            String media_type
    );

    Optional<List<MediaDbModel>> findByProductIdAndOrganisationIdAndIsActive(
            Integer product_id,
            UUID organisation_id,
            Boolean is_active
    );

    Optional<MediaDbModel> findByProductIdAndOrganisationIdAndIsActiveAndId(
            Integer product_id,
            UUID organisation_id,
            Boolean is_active,
            Integer id
    );

}
