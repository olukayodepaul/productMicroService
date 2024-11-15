package com.dart.product.service.product_media;


import com.dart.product.dependency.di.ServicesDi;
import com.dart.product.dto_model.product_media_model.*;
import com.dart.product.entity.prodct_media.MediaContentDbEntity;
import com.dart.product.entity.prodct_media.MediaDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductMediaContentRepo;
import com.dart.product.repository.ProductMediaRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
public class CreateProductMediaService {

    private static final Logger logger = LoggerFactory.getLogger(CreateProductMediaService.class);
    private final ProductMediaContentRepo productMediaContentRepo;
    private final ProductMediaRepo productMediaRepo;
    private final MediaService mediaService;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    @Value("${media.maxImages}")
    private int maxImages;

    @Value("${media.maxVideos}")
    private int maxVideos;

    public CreateProductMediaService(
            ProductMediaContentRepo productMediaContentRepo,
            ProductMediaRepo productMediaRepo,
            MediaService mediaService,
            ServicesDi di
    )
    {
        this.productMediaContentRepo = productMediaContentRepo;
        this.productMediaRepo = productMediaRepo;
        this.mediaService = mediaService;
        this.jwtService = di.jwtService();
        this.utilitiesManager = di.utilitiesManager();
        this.productMappers = di.productMappers();
        this.redisProductCacheRepo = di.redisProductCacheRepo();
        this.validationUtils = di.validationUtils();
    }

    public ResponseEntity<ProductMediaResDTO> createProductMedia(String authToken, MultipartFile file, Integer productId) throws IOException {

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));

        validateBruteForceProtection(userId.toString());
        validateRequestToken(authToken);
        validProductId(productId);

        String roles = jwtService.extractRole(jwtToken);
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validateUserRole(roles);

        Optional<MediaDbEntity> isProductMediaIdPresentInDb = confirmIfProductMediaContentIsPresentInDb(productId, organisationId);
        int setProductMediaId = 0;

        if (isProductMediaIdPresentInDb.isPresent()) {
            setProductMediaId = isProductMediaIdPresentInDb.get().getId();
        } else {

            MediaDbEntity persistProductMediaId = MediaDbEntity.builder()
                    .id(0)
                    .productId(productId)
                    .organisationId(organisationId)
                    .createdAt(LocalDateTime.now())
                    .build();

            ProductMedia getAutoId = saveProductMediaContent(persistProductMediaId);
            boolean cacheProductMedia = redisProductCacheRepo.saveUpdateProductMedia(productMappers.mapProductMediaCacheEntityToMediaDbEntity(getAutoId.getProductMedia()));
            checkCacheForMediaContent(cacheProductMedia);
            setProductMediaId = getAutoId.getProductMedia().getId();
        }

        validateMediaCount(productId, organisationId, file.getContentType().split("/")[0]);

        MediaUploadReqModel mediaData = new MediaUploadReqModel();
        MediaUploadResponse uploadMedia = mediaService.uploadFile(file);

        boolean mediaState = findByProductIdAndOrganisationIdAndMediaTypeAndIsPrimaryAndIsActive(productId, organisationId, uploadMedia.getMediaType()).isPresent();

        mediaData.setCreated_at(LocalDateTime.now());
        mediaData.setUpdated_at(LocalDateTime.now());
        mediaData.setMedia_url(uploadMedia.getFileName());
        mediaData.setIsActive(true);
        mediaData.setMedia_type(uploadMedia.getMediaType());
        mediaData.setPrimary(!mediaState);
        mediaData.setOrganisation_id(organisationId);
        mediaData.setProduct_id(productId);
        mediaData.setProduct_media_id(setProductMediaId);
        mediaData.setId(0);

        SaveAndUpdateMediaResponse persistRecord = saveProductContentMedia(productMappers.toProductMedia(mediaData), uploadMedia.getFileName());
        checkIfRecordPersisted(persistRecord);

        boolean cacheResult = redisProductCacheRepo.saveUpdateProductMediaContent(productMappers.toCacheProductMedia(persistRecord.getProductMedia()));
        checkCacheForMediaContent(cacheResult);

        return new ResponseEntity<>(productMappers.toProductMediaResponse(persistRecord.getProductMedia(), AppConfig.PRODUCT_MEDIA_RESPONSE), HttpStatus.CREATED);
    }

    private void validProductId(Integer productId) {
        validationUtils.validProductId(productId);
    }

    private void checkCacheForMediaContent(boolean isRecord) {
        if (!isRecord) {
            publishKafkaMessage("ProductFeedbackCacheFailure", "Failed to save feedback in cache for product ID: ");
        }
    }

    private void publishKafkaMessage(String topic, String message) {
        // Logic for publishing a message to Kafka
    }

    private void validateRequestToken(String token) {
        validationUtils.accessTokenValidation(token);
    }

    private void validateUserRole(String role) {
        validationUtils.adminRoleValidation(role);
    }

    private void validateBruteForceProtection(String userId) {
        validationUtils.bruteForceProtection(AppConfig.CREATE_PRODUCT_MEDIA_BRUTE_FORCE_PROTECTION + userId);
    }

    private Optional<MediaContentDbEntity> findByProductIdAndOrganisationIdAndMediaTypeAndIsPrimaryAndIsActive(Integer productId, UUID organisationId, String mediaType) {
        return productMediaContentRepo.findByProductIdAndOrganisationIdAndMediaTypeAndIsPrimaryAndIsActive(productId, organisationId, mediaType, true, true);
    }

    private void checkIfRecordPersisted(SaveAndUpdateMediaResponse isSave) {
        if (!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private SaveAndUpdateMediaResponse saveProductContentMedia(MediaContentDbEntity regDetails, String deleteMedia) {
        try {
            return new SaveAndUpdateMediaResponse(true, "", productMediaContentRepo.save(regDetails));
        } catch (Exception e) {
            mediaService.deleteMedia(deleteMedia);
            return new SaveAndUpdateMediaResponse(false, e.getMessage(), MediaContentDbEntity.builder().build());
        }
    }

    private ProductMedia saveProductMediaContent(MediaDbEntity regDetails) {
        try {
            return new ProductMedia(true, "", productMediaRepo.save(regDetails));
        } catch (Exception e) {
            return new ProductMedia(false, e.getMessage(), MediaDbEntity.builder().build());
        }
    }

    private void validateMediaCount(Integer productId, UUID organisationId, String mediaType) {
        long currentMediaCount = productMediaContentRepo.countByProductIdAndOrganisationIdAndMediaTypeAndIsActive(productId, organisationId, mediaType, true);
        if ("image".equalsIgnoreCase(mediaType) && currentMediaCount >= maxImages) {
            throw new CustomRuntimeException(new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), AppConfig.PRODUCT_MEDIA_MAX_IMAGE), HttpStatus.BAD_REQUEST);
        }
        if ("video".equalsIgnoreCase(mediaType) && currentMediaCount >= maxVideos) {
            throw new CustomRuntimeException(new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), AppConfig.PRODUCT_MEDIA_MAX_VIDEO), HttpStatus.BAD_REQUEST);
        }
    }

    private Optional<MediaDbEntity> confirmIfProductMediaContentIsPresentInDb(Integer productId, UUID organisationId) {
        return productMediaRepo.findByProductIdAndOrganisationId(productId, organisationId);
    }

}
