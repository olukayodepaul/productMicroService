package com.dart.product.service.product_media;


import com.dart.product.dto_model.product_media_model.*;
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
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


//first uploaded image or picture if primary image or picture
@Service
public class CreateProductMediaService {

    @Value("${media.maxImages}")
    private int maxImages;

    @Value("${media.maxVideos}")
    private int maxVideos;

    private final MediaService mediaService;
    private final ProductMappers productMappers;
    private final UtilitiesManager utilitiesManager;
    private final ValidationUtils validationUtils;
    private final FilterService jwtService;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ProductMediaRepo productMediaRepo;

    public CreateProductMediaService(
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

    public ResponseEntity<ProductMediaResDTO> createProductMedia(String authToken, MultipartFile file, Integer productId) throws IOException {

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        validateBruteForceProtection(userId.toString()); //reduce number of process before brute force protection
        validateRequestToken(authToken);
        validProductId(productId);
        String roles = jwtService.extractRole(jwtToken);
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validateUserRole(roles);
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
        mediaData.setId(0);

        SaveAndUpdateMediaResponse persistRecord = saveProductMedia(productMappers.toProductMedia(mediaData),uploadMedia.getFileName());
        checkIfRecordPersisted(persistRecord);

        boolean cacheResult = redisProductCacheRepo.saveUpdateProductMedia(productMappers.toCacheProductMedia(persistRecord.getProductMedia()));
        checkIfRecordCache(cacheResult);

        return new ResponseEntity<>(productMappers.toProductMediaResponse(persistRecord.getProductMedia(), AppConfig.PRODUCT_MEDIA_RESPONSE), HttpStatus.CREATED);
    }

    private void validProductId(Integer productId) {
        validationUtils.validProductId(productId);
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
        validationUtils.bruteForceProtection(AppConfig.CREATE_PRODUCT_MEDIA_BRUTE_FORCE_PROTECTION + userId);
    }

    private Optional<MediaDbEntity> findByProductIdAndOrganisationIdAndMediaTypeAndIsPrimaryAndIsActive(Integer productId, UUID organisationId, String mediaType) {
        return productMediaRepo.findByProductIdAndOrganisationIdAndMediaTypeAndIsPrimaryAndIsActive(productId, organisationId, mediaType, true, true);
    }

    private void checkIfRecordPersisted(SaveAndUpdateMediaResponse isSave) {
        if (!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private SaveAndUpdateMediaResponse saveProductMedia(MediaDbEntity regDetails, String deleteMedia) {
        try {
            return new SaveAndUpdateMediaResponse(true, "", productMediaRepo.save(regDetails));
        } catch (Exception e) {
            mediaService.deleteMedia(deleteMedia);
            return new SaveAndUpdateMediaResponse(false, e.getMessage(), MediaDbEntity.builder().build());
        }
    }

    private void validateMediaCount(Integer productId, UUID organisationId, String mediaType) {
        long currentMediaCount = productMediaRepo.countByProductIdAndOrganisationIdAndMediaTypeAndIsActive(productId, organisationId, mediaType, true);
        System.out.println(currentMediaCount);
        if ("image".equalsIgnoreCase(mediaType) && currentMediaCount >= maxImages) {
            throw new CustomRuntimeException(new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), AppConfig.PRODUCT_MEDIA_MAX_IMAGE), HttpStatus.BAD_REQUEST);
        }
        if ("video".equalsIgnoreCase(mediaType) && currentMediaCount >= maxVideos) {
            throw new CustomRuntimeException(new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), AppConfig.PRODUCT_MEDIA_MAX_VIDEO), HttpStatus.BAD_REQUEST);
        }
    }

}
