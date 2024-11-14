package com.dart.product.repository;


import com.dart.product.entity.prodct_media.MediaDbEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ProductMediaRepo extends JpaRepository<MediaDbEntity, Long> {
   Optional<MediaDbEntity> findByProductIdAndOrganisationId(Integer product_id, UUID organisationId);
   Optional<Page<MediaDbEntity>> findByOrganisationId(UUID organisation_id, Pageable pageable);
}
