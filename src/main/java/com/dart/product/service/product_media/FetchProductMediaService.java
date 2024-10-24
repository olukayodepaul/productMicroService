package com.dart.product.service.product_media;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_media_model.FetchAllProductMediaModel;
import com.dart.product.entity.product_media_model.GetAllMediaModel;
import com.dart.product.entity.product_media_model.MediaDbModel;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FetchProductMediaService {

    private final ServiceLocator serviceLocator;
    private static final String IMAGE_MEDIA_TYPE = "image";
    private static final String VIDEO_MEDIA_TYPE = "video";

    public FetchProductMediaService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<GetAllMediaModel> getProductMedia(String token, Integer productId) {

        validateRequestToken(token);
        validateProductId(productId);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);
        validateBruteForceProtection(plainUUID);

        // Fetch from the cache
        FetchAllProductMediaModel cachedProductMedia = serviceLocator.getRedisProductCacheRepo().findAllProductMedia(organisationId.toString(), productId.toString());

        if (cachedProductMedia.getStatus()) {
            System.out.println(1);
            List<GetAllMediaModel.ImageMedia> imageMediaList = serviceLocator.getProductMappers().filterAndMapCacheMediaImage(cachedProductMedia.getProductMedia(), IMAGE_MEDIA_TYPE);
            List<GetAllMediaModel.VideoMedia> videoMediaList = serviceLocator.getProductMappers().filterAndMapCacheMediaVideo(cachedProductMedia.getProductMedia(), VIDEO_MEDIA_TYPE);
            return buildResponse(imageMediaList, videoMediaList, productId);
        } else {
            List<MediaDbModel> productMediaFromDb = findMediaByProductIdAndOrganisationId(productId, organisationId);
            validateIfProductMediaExists(productMediaFromDb);
            serviceLocator.getRedisProductCacheRepo().saveAllProductMedia(serviceLocator.getProductMappers().mapCacheProductMedia(productMediaFromDb));
            List<GetAllMediaModel.ImageMedia> imageMediaList = serviceLocator.getProductMappers().filterAndMapMediaImage(productMediaFromDb, IMAGE_MEDIA_TYPE);
            List<GetAllMediaModel.VideoMedia> videoMediaList = serviceLocator.getProductMappers().filterAndMapMediaVideo(productMediaFromDb, VIDEO_MEDIA_TYPE);
            return buildResponse(imageMediaList, videoMediaList, productId);
        }
    }

    private ResponseEntity<GetAllMediaModel> buildResponse(List<GetAllMediaModel.ImageMedia> imageMediaList, List<GetAllMediaModel.VideoMedia> videoMediaList, Integer productId) {
        return new ResponseEntity<>(
                new GetAllMediaModel(
                        true,
                        "Product media successfully fetched",
                        productId,
                        true,
                        imageMediaList,
                        videoMediaList
                ),
                HttpStatus.OK
        );
    }

    private void validateRequestToken(String token) {
        serviceLocator.getValidationUtils().jwtValidateRequest(token);
    }

    private void validateBruteForceProtection(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private void validateProductId(Integer productId) {
        serviceLocator.getValidationUtils().productIdValidation(productId);
    }

    private void validateIfProductMediaExists(List<MediaDbModel> productMedia) {
        if (productMedia.isEmpty()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, HttpStatus.NOT_FOUND.toString(), AppConfig.FETCH_ALL_PRODUCT_MEDIA),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    /**
     * Fetch media by product ID and organisation ID.
     *
     * @param productId      Product ID
     * @param organisationId Organisation ID
     * @return List of MediaDbModel
     */
    private List<MediaDbModel> findMediaByProductIdAndOrganisationId(Integer productId, UUID organisationId) {
        return serviceLocator.getProductMediaRepo()
                .findByProductIdAndOrganisationIdAndIsActive(productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }
}
