package com.dart.product.service.product_media;


import com.dart.product.dto_model.product_media_model.PrimaryProductResDTO;
import com.dart.product.dto_model.product_media_model.SaveAndUpdateMediaResponse;
import com.dart.product.entity.prodct_media.MediaDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductMediaRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UpdatePrimaryProductService {

    private final ProductMappers productMappers;
    private final UtilitiesManager utilitiesManager;
    private final ValidationUtils validationUtils;
    private final FilterService jwtService;
    private final ProductMediaRepo productMediaRepo;
    private final RedisProductCacheRepo redisProductCacheRepo;

    public UpdatePrimaryProductService(
            UtilitiesManager utilitiesManager,
            ValidationUtils validationUtils,
            FilterService jwtService,
            ProductMappers productMappers,
            ProductMediaRepo productMediaRepo,
            RedisProductCacheRepo redisProductCacheRepo
    ) {
        this.utilitiesManager = utilitiesManager;
        this.validationUtils = validationUtils;
        this.jwtService = jwtService;
        this.productMappers = productMappers;
        this.productMediaRepo = productMediaRepo;
        this.redisProductCacheRepo = redisProductCacheRepo;
    }


    @Transactional
    public ResponseEntity<PrimaryProductResDTO> updatePrimaryMedia(String authToken, Integer productId, Integer mediaId) {

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validateBruteForceProtection(userId.toString());
        validateRequestToken(authToken);
        validProductId(productId);
        validMediaId(mediaId);
        validateUserRole(roles);

        MediaDbEntity setToCurrentMedia = findPreviousMedia(mediaId, productId, organisationId);
        validateIfRecordIfPrimaryMedia(setToCurrentMedia.getIsPrimary());
        MediaDbEntity setToPreviousMedia = findCurrentMedia(productId, organisationId, setToCurrentMedia.getMediaType());

        setToCurrentMedia.setUpdatedAt(LocalDateTime.now());
        setToCurrentMedia.setIsPrimary(true);
        saveProductMedia(setToCurrentMedia);

        setToPreviousMedia.setIsPrimary(false);
        setToPreviousMedia.setUpdatedAt(LocalDateTime.now());
        saveProductMedia(setToPreviousMedia);

        boolean cacheCurrentMedia = redisProductCacheRepo.saveUpdateProductMedia(productMappers.toCacheProductMedia(setToCurrentMedia));
        checkIfRecordCache(cacheCurrentMedia);

        boolean cachePreviousMedia = redisProductCacheRepo.saveUpdateProductMedia(productMappers.toCacheProductMedia(setToPreviousMedia));
        checkIfRecordCache(cachePreviousMedia);

        return new ResponseEntity<>(productMappers.productMediaBuilder(setToCurrentMedia, setToPreviousMedia, AppConfig.PRODUCT_PRIMARY_MEDIA_UPDATED_RESPONSE), HttpStatus.OK);
    }

    private void checkIfRecordCache(boolean isRecord) {
        if (!isRecord) {
            publishKafkaMessage("ProductFeedbackCacheFailure", "Failed to save feedback in cache for product ID: ");
        }
    }

    private void publishKafkaMessage(String topic, String message) {
        // Logic for publishing a message to Kafka
    }

    private void validateIfRecordIfPrimaryMedia(Boolean media) {
        if (media) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), AppConfig.PRODUCT_PRIMARY_UPDATE),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateRequestToken(String token) {
        validationUtils.accessTokenValidation(token);
    }

    private void validateUserRole(String role) {
        validationUtils.adminRoleValidation(role);
    }

    private void validProductId(Integer productId) {
        validationUtils.validProductId(productId);
    }

    private void validateBruteForceProtection(String userId) {
        validationUtils.bruteForceProtection(AppConfig.UPDATE_PRIMARY_PRODUCT_MEDIA_BRUTE_FORCE_PROTECTION + userId);
    }

    private void validMediaId(Integer mediaId) {
        validationUtils.mediaIdValidation(mediaId);
    }

    private MediaDbEntity findPreviousMedia(Integer mediaId, Integer productId, UUID organisationId) {
        return productMediaRepo.findByIdAndProductIdAndOrganisationIdAndIsActive(mediaId, productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(

                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.UPDATE_RESOURCES_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    private MediaDbEntity findCurrentMedia(Integer productId, UUID organisationId, String mediaType) {
        return productMediaRepo.findByProductIdAndOrganisationIdAndMediaTypeAndIsPrimaryAndIsActive(productId, organisationId, mediaType, true, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.UPDATE_RESOURCES_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    public SaveAndUpdateMediaResponse saveProductMedia(MediaDbEntity regDetails) {
        try {
            return new SaveAndUpdateMediaResponse(true, "", productMediaRepo.save(regDetails));
        } catch (Exception e) {
            return new SaveAndUpdateMediaResponse(false, e.getMessage(), MediaDbEntity.builder().build());
        }
    }

}
