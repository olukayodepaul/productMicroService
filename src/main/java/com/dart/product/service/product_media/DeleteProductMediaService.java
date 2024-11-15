package com.dart.product.service.product_media;

import com.dart.product.dependency.di.ServicesDi;
import com.dart.product.dto_model.product_media_model.ProductMediaResDTO;
import com.dart.product.dto_model.product_media_model.SaveAndUpdateMediaResponse;
import com.dart.product.entity.prodct_media.MediaContentDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductMediaContentRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeleteProductMediaService {

    private static final Logger logger = LoggerFactory.getLogger(DeleteProductMediaService.class);
    private final ProductMediaContentRepo productMediaContentRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    public DeleteProductMediaService(
            ProductMediaContentRepo productMediaContentRepo,
            ServicesDi servicesDi
    )
    {
        this.productMediaContentRepo = productMediaContentRepo;

        this.jwtService = servicesDi.jwtService();
        this.utilitiesManager = servicesDi.utilitiesManager();
        this.productMappers = servicesDi.productMappers();
        this.redisProductCacheRepo = servicesDi.redisProductCacheRepo();
        this.validationUtils = servicesDi.validationUtils();
    }

    public ResponseEntity<ProductMediaResDTO> deleteProductMedia(String authToken, Integer mediaId) {

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        validateBruteForceProtection(userId.toString());
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validateRequestToken(authToken);
        validMediaId(mediaId);
        validateUserRole(roles);

        MediaContentDbEntity getPersistedProduct = getPersistedProductMedia(mediaId, organisationId);
        validateIfPrimaryMedia(getPersistedProduct.getIsPrimary());

        getPersistedProduct.setUpdatedAt(LocalDateTime.now());
        getPersistedProduct.setIsActive(false);
        SaveAndUpdateMediaResponse persistRecord = saveProductMedia(getPersistedProduct);
        checkIfRecordPersisted(persistRecord);

        boolean cacheResult = redisProductCacheRepo.saveUpdateProductMediaContent(productMappers.toCacheProductMedia(persistRecord.getProductMedia()));
        checkIfRecordCache(cacheResult);

        return new ResponseEntity<>(productMappers.toProductMediaResponse(persistRecord.getProductMedia(), AppConfig.DELETE_MEDIA_UPDATED_RESPONSE), HttpStatus.OK);

    }

    private void checkIfRecordCache(boolean isRecord) {
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
        validationUtils.bruteForceProtection(AppConfig.DELETE_PRODUCT_MEDIA_BRUTE_FORCE_PROTECTION + userId);
    }

    private void validMediaId(Integer mediaId) {
        validationUtils.mediaIdValidation(mediaId);
    }

    private void validateIfPrimaryMedia(boolean isActive) {
        if (!isActive) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, HttpStatus.NOT_FOUND.toString(), AppConfig.DELETE_PRIMARY_MEDIA_RESPONSE),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    private MediaContentDbEntity getPersistedProductMedia(Integer mediaId, UUID organisationId) {
        return productMediaContentRepo.findByIdAndOrganisationIdAndIsActive(mediaId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(

                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETE_RESOURCES_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    private SaveAndUpdateMediaResponse saveProductMedia(MediaContentDbEntity regDetails) {
        try {
            return new SaveAndUpdateMediaResponse(true, "", productMediaContentRepo.save(regDetails));
        } catch (Exception e) {
            return new SaveAndUpdateMediaResponse(false, e.getMessage(), MediaContentDbEntity.builder().build());
        }
    }

    private void checkIfRecordPersisted(SaveAndUpdateMediaResponse isSave) {
        if (!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), AppConfig.PRODUCT_MEDIA_DELETED),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
