package com.dart.product.service.product_media;


import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_media_model.*;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
public class GetIndividualProductMediaService {

    private final ServiceLocator serviceLocator;

    public GetIndividualProductMediaService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<GetIndividualProductMediaModel> getProductMediaByMediaId(String token, Integer productId, Integer mediaId) {

        validateRequestToken(token);
        validateProductId(productId);
        validateRequestMediaId(mediaId);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);
        validateBruteForceProtection(plainUUID);

        FetchIndividualMediaProductModel cachedProductMedia = serviceLocator.getRedisProductCacheRepo().findOneProductMedia(organisationId.toString(), productId, mediaId);

        if(cachedProductMedia.getStatus()) {
            return buildResponse(serviceLocator.getProductMappers().mapSingleProductMediaToCache(cachedProductMedia.getProductMedia()));
        }else{
            MediaDbModel productMediaFromDb = findByProductIdAndOrganisationIdAndIsActiveAndId(productId, organisationId, mediaId);
            return buildResponse(productMediaFromDb);
        }

    }

    private void validateRequestToken(String token) {
        serviceLocator.getValidationUtils().jwtValidateRequest(token);
    }

    private void validateProductId(Integer productId) {
        serviceLocator.getValidationUtils().productIdValidation(productId);
    }

    private void validateRequestMediaId(Integer mediaId) {
        serviceLocator.getValidationUtils().mediaIdValidation(mediaId);
    }

    private void validateBruteForceProtection(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private MediaDbModel findByProductIdAndOrganisationIdAndIsActiveAndId(Integer productId, UUID organisationId, Integer mediaId) {
        return serviceLocator.getProductMediaRepo()
                .findByProductIdAndOrganisationIdAndIsActiveAndId(productId, organisationId, true, mediaId)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    private ResponseEntity<GetIndividualProductMediaModel> buildResponse(MediaDbModel productMedia) {
        return new ResponseEntity<>(
                new GetIndividualProductMediaModel(
                        true,
                        "Product media successfully fetched",
                        serviceLocator.getProductMappers().filterAndMapSingleProductMedia(productMedia)
                ),
                HttpStatus.OK
        );
    }


}
