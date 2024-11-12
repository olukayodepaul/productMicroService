package com.dart.product.service.product_media;

import com.dart.product.dto_model.product_media_model.MediaUploadReqModel;
import com.dart.product.dto_model.product_media_model.MediaUploadResponse;
import com.dart.product.dto_model.product_media_model.ProductMediaResDTO;
import com.dart.product.dto_model.product_media_model.SaveAndUpdateMediaResponse;
import com.dart.product.entity.prodct_media.MediaDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductMediaRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class UpdateProductMediaService {

    private final MediaService mediaService;
    private final ProductMappers productMappers;
    private final UtilitiesManager utilitiesManager;
    private final ValidationUtils validationUtils;
    private final FilterService jwtService;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ProductMediaRepo productMediaRepo;

    public UpdateProductMediaService(
            UtilitiesManager utilitiesManager,
            ValidationUtils validationUtils,
            FilterService jwtService,
            MediaService mediaService,
            ProductMappers productMappers,
            RedisProductCacheRepo redisProductCacheRepo,
            ProductMediaRepo productMediaRepo
    ) {
        this.utilitiesManager = utilitiesManager;
        this.validationUtils = validationUtils;
        this.jwtService = jwtService;
        this.mediaService = mediaService;
        this.productMappers = productMappers;
        this.redisProductCacheRepo = redisProductCacheRepo;
        this.productMediaRepo = productMediaRepo;
    }


    public ResponseEntity<ProductMediaResDTO> updateProductMedia(String authToken, MultipartFile file, Integer productId, Integer mediaId) throws IOException {

        validateRequestToken(authToken);
        validProductId(productId);
        validMediaId(mediaId);

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validateUserRole(roles);
        validateBruteForceProtection(userId.toString());

        MediaUploadReqModel mediaData = new MediaUploadReqModel();
        MediaDbEntity getPersistedMedia = getPersistedProductMedia(mediaId, productId, organisationId);

        compareFormalAgainstCurrentMediaType(utilitiesManager.getFileExtension(file.getOriginalFilename()), getPersistedMedia.getMediaType(), file);
        MediaUploadResponse uploadMedia = mediaService.uploadFile(file);
        String mediaToDelete = getPersistedMedia.getMediaUrl();

        mediaData.setCreated_at(getPersistedMedia.getCreatedAt());
        mediaData.setUpdated_at(LocalDateTime.now());
        mediaData.setMedia_url(uploadMedia.getFileName());
        mediaData.setIsActive(getPersistedMedia.getIsActive());
        mediaData.setMedia_type(uploadMedia.getMediaType());
        mediaData.setPrimary(getPersistedMedia.getIsPrimary());
        mediaData.setOrganisation_id(getPersistedMedia.getOrganisationId());
        mediaData.setProduct_id(getPersistedMedia.getProductId());
        mediaData.setId(getPersistedMedia.getId());

        SaveAndUpdateMediaResponse persistRecord = saveProductMedia(productMappers.toProductMedia(mediaData), uploadMedia.getFileName());
        checkIfRecordPersisted(persistRecord);

        mediaService.deleteMedia(mediaToDelete);

        boolean cacheResult = redisProductCacheRepo.saveUpdateProductMedia(productMappers.toCacheProductMedia(persistRecord.getProductMedia()));
        checkIfRecordCache(cacheResult);

        return new ResponseEntity<>(productMappers.toProductMediaResponse(persistRecord.getProductMedia(), AppConfig.PRODUCT_MEDIA_UPDATED_RESPONSE), HttpStatus.OK);
    }

    private void checkIfRecordCache(boolean isRecord) {
        if (!isRecord) {
            publishKafkaMessage("ProductFeedbackCacheFailure", "Failed to save feedback in cache for product ID: ");
        }
    }

    private void publishKafkaMessage(String topic, String message) {
        // Logic for publishing a message to Kafka
    }

    private void compareFormalAgainstCurrentMediaType(String mediaExtension, String oldFileMediaType, MultipartFile file) {
        if (!validateMediaType(mediaExtension, oldFileMediaType, file)) {
            String errorMessage = (oldFileMediaType.equalsIgnoreCase("video"))
                    ? AppConfig.PRODUCT_MEDIA_MAX_IMAGE_REPLACEMENT
                    : AppConfig.PRODUCT_MEDIA_MAX_VIDEO_REPLACEMENT;

            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), errorMessage),
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
        validationUtils.bruteForceProtection(AppConfig.UPDATE_PRODUCT_MEDIA_BRUTE_FORCE_PROTECTION + userId);
    }

    private void validMediaId(Integer mediaId) {
        validationUtils.mediaIdValidation(mediaId);
    }

    private boolean validateMediaType(String mediaExtension, String oldFileMediaType, MultipartFile file) {

        if (oldFileMediaType.equalsIgnoreCase("image")) {
            if (file.getSize() > AppConfig.MAX_IMAGE_SIZE) {
                throw new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), AppConfig.MAX_SIZE_IMAGE),
                        HttpStatus.BAD_REQUEST
                );
            }
        } else if (oldFileMediaType.equalsIgnoreCase("video")) {
            if (file.getSize() > AppConfig.MAX_VIDEO_SIZE) {
                throw new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), AppConfig.MAX_SIZE_VIDEO),
                        HttpStatus.BAD_REQUEST
                );
            }
        }

        if (oldFileMediaType.equalsIgnoreCase("video")) {
            return utilitiesManager.isVideoFile(mediaExtension);
        }

        if (oldFileMediaType.equalsIgnoreCase("image")) {
            return utilitiesManager.isImageFile(mediaExtension);
        }

        return false;
    }

    private SaveAndUpdateMediaResponse saveProductMedia(MediaDbEntity regDetails, String deleteMedia) {
        try {
            return new SaveAndUpdateMediaResponse(true, "", productMediaRepo.save(regDetails));
        } catch (Exception e) {
            mediaService.deleteMedia(deleteMedia);
            return new SaveAndUpdateMediaResponse(false, e.getMessage(), MediaDbEntity.builder().build());
        }
    }

    private void checkIfRecordPersisted(SaveAndUpdateMediaResponse isSave) {
        if (!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private MediaDbEntity getPersistedProductMedia(Integer mediaId, Integer productId, UUID organisationId) {
        return productMediaRepo.findByIdAndProductIdAndOrganisationIdAndIsActive(mediaId, productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(

                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.INVALID_RESOURCES_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}
