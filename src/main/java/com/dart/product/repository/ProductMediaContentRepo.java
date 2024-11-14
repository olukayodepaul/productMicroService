package com.dart.product.repository;

import com.dart.product.entity.prodct_media.MediaContentDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ProductMediaContentRepo extends JpaRepository<MediaContentDbEntity, Long> {
    Optional<MediaContentDbEntity> findByProductIdAndOrganisationIdAndMediaTypeAndIsPrimaryAndIsActive(Integer product_id, UUID organisation_id, String media_type, boolean is_primary, boolean is_active);
    Long countByProductIdAndOrganisationIdAndMediaTypeAndIsActive(Integer product_id, UUID organisation_id, String media_type, boolean is_active);
    Optional<MediaContentDbEntity> findByIdAndProductIdAndOrganisationIdAndIsActive(Integer id, Integer product_id, UUID organisation_id, boolean is_active);
    Optional<List<MediaContentDbEntity>> findByProductIdAndOrganisationIdAndMediaTypeAndIsActiveOrderByIdAsc(Integer product_id, UUID organisation_id, String media_type, boolean is_active);
    Optional<MediaContentDbEntity> findByIdAndOrganisationIdAndIsActive(Integer id, UUID organisation_id, boolean is_active);
    Optional<List<MediaContentDbEntity>> findByProductIdAndOrganisationIdAndIsActiveOrderByIdAsc(Integer product_id, UUID organisation_id, boolean is_active);
    Optional<List<MediaContentDbEntity>> findByOrganisationIdAndIsActive(UUID organisation_id, boolean is_active);
    Optional<List<MediaContentDbEntity>> findByOrganisationIdAndIsActiveAndProductIdIn(UUID organisationId, boolean isActive, List<Integer> productIds);
}
