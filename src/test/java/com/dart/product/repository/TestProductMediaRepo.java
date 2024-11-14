package com.dart.product.repository;


import com.dart.product.entity.prodct_media.MediaContentDbEntity;
import com.dart.product.utilities.UtilitiesManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TestProductMediaRepo {

    @Autowired
    private ProductMediaContentRepo productMediaRepo;

    @MockBean
    private UtilitiesManager utilitiesManager;

    @Test
    public void ProductMediaRepo_saveAll_returnSaveRepository() {

        //Arrange
        MediaContentDbEntity mediaDbEntity =
                MediaContentDbEntity.builder()
                        .id(1)
                        .productId(6)
                        .organisationId(utilitiesManager.convertStringToUUID("e41bfaef-d028-352d-ae1a-026a775959d4"))
                        .mediaType("video")
                        .mediaUrl("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1dWlkIjoiNzI1ZTk4NTAtY2NmMi00NDFhLThkNzctMDJjMmExNjA0NDYyIiwidGltZXN0YW1wIjoxNzMxMjI1NzYzMzU1fQ.fMzkYD9XIHAi3QuL6HX1qt40655DBctkPxLmHComPg8.png")
                        .isPrimary(false)
                        .isActive(true)
                        .updatedAt(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .build();

        //act
        MediaContentDbEntity savedEntity =
                productMediaRepo.save(mediaDbEntity);

        //assert
        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedEntity.getId(), "ID should not be null"),
                () -> Assertions.assertEquals(mediaDbEntity.getProductId(), savedEntity.getProductId()),
                () -> Assertions.assertEquals(mediaDbEntity.getMediaType(), savedEntity.getMediaType()),
                () -> Assertions.assertEquals(mediaDbEntity.getMediaUrl(), savedEntity.getMediaUrl())
        );
    }

    @Test
    public void ProductMediaRepo_find_returnSaveRepository() {
        Optional<List<MediaContentDbEntity>>  response = productMediaRepo
                .findByProductIdAndOrganisationIdAndIsActiveOrderByIdAsc(6, utilitiesManager.convertStringToUUID("e41bfaef-d028-352d-ae1a-026a775959d4"), true);
        Assertions.assertAll(
                ()->Assertions.assertNotNull(response.toString(), "ID should not be null")
        );
    }

    @Test
    public void ProductMediaRepo_ByOrganisationIdAndIsActive() {
        UUID organisationId = utilitiesManager.convertStringToUUID("e41bfaef-d028-352d-ae1a-026a775959d4");
        boolean isTrue = true;
        Pageable pageable =  PageRequest.of(0 / 10, 10, Sort.by(Sort.Direction.ASC, "id"));
        Optional<Page<MediaContentDbEntity>> response = productMediaRepo.findByOrganisationIdAndIsActive(organisationId, isTrue, pageable);
        Assertions.assertAll(
                ()->Assertions.assertNotNull(response.toString(), "ID should not be null")
        );
    }

}
