package com.dart.product.service.product_media;


import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_media_model.FetchAllProductMediaModel;
import com.dart.product.entity.product_media_model.GetSpecMediaModel;
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
public class GetSpecificProductMediaService {

    private final ServiceLocator serviceLocator;

    public GetSpecificProductMediaService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<GetSpecMediaModel> getProductMediaByMediaType(String token, Integer productId, String mediaType){

        validateRequestToken(token);
        validateProductId(productId);
        validateRequestMediaType(mediaType);
        isMediaTypeImageOrVideo(mediaType);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);
        validateBruteForceProtection(plainUUID);

        FetchAllProductMediaModel cachedProductMedia = serviceLocator.getRedisProductCacheRepo().findAllProductMedia(organisationId.toString(), productId.toString());

        if (cachedProductMedia.getStatus()) {

            List<GetSpecMediaModel.Media> mediaList = serviceLocator
                    .getProductMappers()
                    .filterAndMapMedia(serviceLocator.getProductMappers().mapProductMedia(cachedProductMedia.getProductMedia()), mediaType);

            validateEmptyMediaProduct(mediaList);
            return  buildResponse(mediaList, productId , mediaType);

        } else {

            List<MediaDbModel> productMediaFromDb = findMediaByProductIdAndOrganisationId(productId, organisationId);
            validateIfProductMediaExists(productMediaFromDb);
            serviceLocator.getRedisProductCacheRepo().saveAllProductMedia(serviceLocator.getProductMappers().mapCacheProductMedia(productMediaFromDb));

            List<GetSpecMediaModel.Media> mediaList = serviceLocator
                    .getProductMappers()
                    .filterAndMapMedia(productMediaFromDb, mediaType);

            validateEmptyMediaProduct(mediaList);
            return  buildResponse(mediaList, productId , mediaType);

        }
    }

    private void validateIfProductMediaExists(List<MediaDbModel> productMedia) {
        if (productMedia.isEmpty()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, HttpStatus.NOT_FOUND.toString(), AppConfig.FETCH_ALL_PRODUCT_MEDIA),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    private List<MediaDbModel> findMediaByProductIdAndOrganisationId(Integer productId, UUID organisationId) {
        return serviceLocator.getProductMediaRepo()
                .findByProductIdAndOrganisationIdAndIsActive(productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    private ResponseEntity<GetSpecMediaModel> buildResponse(List<GetSpecMediaModel.Media> imageMediaList, Integer productId, String mediaType) {
        return new ResponseEntity<>(
                new GetSpecMediaModel(
                        true,
                        "Product media successfully fetched",
                        productId,
                        true,
                        mediaType,
                        imageMediaList
                ),
                HttpStatus.OK
        );
    }

    private void validateRequestToken(String token) {
        serviceLocator.getValidationUtils().jwtValidateRequest(token);
    }

    private void validateEmptyMediaProduct(List<GetSpecMediaModel.Media> isEmptyProductMedia) {
        if(isEmptyProductMedia.isEmpty()){
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), "Empty media type"),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    private void validateProductId(Integer productId) {
        serviceLocator.getValidationUtils().productIdValidation(productId);
    }

    private void validateBruteForceProtection(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private void validateRequestMediaType(String mediaType) {
        serviceLocator.getValidationUtils().mediaTypeValidation(mediaType);
    }

    private void isMediaTypeImageOrVideo(String mediaType) {
        if (!mediaType.equalsIgnoreCase("video") && !mediaType.equalsIgnoreCase("image")) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), "Invalid media type: " + mediaType + ".Only 'video' and 'image' types are allowed"),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
