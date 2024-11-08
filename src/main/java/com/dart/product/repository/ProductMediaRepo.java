package com.dart.product.repository;

import com.dart.product.entity.prodct_media.MediaDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ProductMediaRepo extends JpaRepository<MediaDbEntity, Long> {

    Optional<MediaDbEntity> findByProductIdAndOrganisationIdAndMediaTypeAndIsPrimaryAndIsActive(Integer product_id, UUID organisation_id, String media_type, boolean is_primary, boolean is_active);
    Long countByProductIdAndOrganisationIdAndMediaTypeAndIsActive(Integer product_id, UUID organisation_id, String media_type, boolean is_active);
    Optional<MediaDbEntity> findByIdAndProductIdAndOrganisationIdAndIsActive(Integer id, Integer product_id, UUID organisation_id,  boolean is_active);
    Optional<List<MediaDbEntity>> findByProductIdAndOrganisationIdAndMediaTypeAndIsActive(Integer product_id, UUID organisation_id, String media_type, boolean is_active);

}
