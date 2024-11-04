package com.dart.product.repository;

import com.dart.product.entity.prodct_media.MediaDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ProductMediaRepo extends JpaRepository<MediaDbEntity, Long> {

    Optional<MediaDbEntity> findByProductIdAndOrganisationIdAndMediaTypeAndIsPrimary(Integer product_id, UUID organisation_id, String media_type, boolean is_primary);
    Long countByProductIdAndOrganisationIdAndMediaTypeAndIsActive(Integer product_id, UUID organisation_id, String media_type, boolean is_active);
    Optional<MediaDbEntity> findByIdAndProductIdAndOrganisationIdAndIsActive(Integer id, Integer product_id, UUID organisation_id,  boolean is_active);

//    Optional<MediaDbModel> findByIdAndOrganisationIdAndIsActive(
//            Integer id,
//            UUID organisation_id,
//            boolean is_primary
//    );
//
//    Optional<MediaDbModel> findByIdAndOrganisationIdAndIsActive(
//            Integer id,
//            UUID organisation_id,
//            Boolean is_active
//    );
//
//    Optional<MediaDbModel> findByProductIdAndIsPrimaryAndOrganisationIdAndMediaType(
//            Integer product_id,
//            boolean is_primary,
//            UUID organisation_id,
//            String media_type
//    );
//
//    Optional<List<MediaDbModel>> findByProductIdAndOrganisationIdAndIsActive(
//            Integer product_id,
//            UUID organisation_id,
//            Boolean is_active
//    );
//
//    Optional<MediaDbModel> findByProductIdAndOrganisationIdAndIsActiveAndId(
//            Integer product_id,
//            UUID organisation_id,
//            Boolean is_active,
//            Integer id
//    );

}
