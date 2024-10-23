package com.dart.product.service.product_media;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_media_model.MediaDbModel;
import com.dart.product.entity.product_media_model.ProductMediaResModel;
import com.dart.product.entity.product_media_model.SaveAndUpdateMediaResponse;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@Service
public class DeleteProductMediaService {

    private final ServiceLocator serviceLocator;

    public DeleteProductMediaService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<ProductMediaResModel> deleteProductMedia(String token, Integer id){

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(serviceLocator.getJwtService().extractUUID(jwtToken));

        validateRequestToken(token);
        validateRequestPrimaryListenBruceForce(plainUUID);

        MediaDbModel isProductMediaExisting = findByIdAndOrganisationId(id, organisationId);
        validIfMediaIsDelete(isProductMediaExisting.getIsActive());
        validateIfMediaIsPrimaryListing(isProductMediaExisting.getIsPrimary());

        isProductMediaExisting.setIsActive(false);
        SaveAndUpdateMediaResponse deleteProductMedia = serviceLocator.getSaveAndUpdateRecord().saveProductMedia(isProductMediaExisting);

        validateIfMediaIsDeleted(deleteProductMedia.getStatus(), deleteProductMedia.getError());

        boolean deleteCache = serviceLocator.getRedisProductCacheRepo().deleteProduct(deleteProductMedia.getProductMedia().getOrganisationId().toString(), deleteProductMedia.getProductMedia().getId());
        handleCacheResult(deleteCache);

        MediaDbModel getDeleteRecord =  deleteProductMedia.getProductMedia();

        return new ResponseEntity<>(
                new ProductMediaResModel(
                        true,
                        "product media successfully deleted",
                        new ProductMediaResModel.ProductMedia(
                                getDeleteRecord.getId(),
                                getDeleteRecord.getProductId(),
                                getDeleteRecord.getMediaType(),
                                getDeleteRecord.getIsPrimary(),
                                getDeleteRecord.getIsActive(),
                                getDeleteRecord.getMediaUrl(),
                                getDeleteRecord.getUpdatedAt(),
                                getDeleteRecord.getCreatedAt()
                        )
                ),
                HttpStatus.OK);

    }

    private void handleCacheResult(boolean cacheResult) {
        if (!cacheResult) {
            System.out.println("dfjwdfjdcdschsw bdcds");
        }
    }

    private void validateRequestToken(String token) {
        serviceLocator.getValidationUtils().jwtValidateRequest(token);
    }

    private void validateRequestPrimaryListenBruceForce(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private void validIfMediaIsDelete(boolean isActive){
        if(!isActive) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, HttpStatus.NOT_FOUND.toString(), AppConfig.UPDATE_PRODUCT_ERROR_RESPONSE),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    private MediaDbModel findByIdAndOrganisationId(Integer id, UUID organisationId) {
        return serviceLocator.getProductMediaRepo().findByIdAndOrganisationIdAndIsActive(id, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, HttpStatus.NOT_FOUND.toString(), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    private void validateIfMediaIsPrimaryListing(boolean primaryListing){
        if(primaryListing) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, HttpStatus.NOT_FOUND.toString(), AppConfig.DELETE_PRIMARY_MEDIA_ERROR_RESPONSE),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    private void validateIfMediaIsDeleted(boolean mediaItems, String message){
        if(!mediaItems){
            throw new CustomRuntimeException(
                    new ErrorHandler(false, HttpStatus.NOT_FOUND.toString(), message),
                    HttpStatus.NOT_FOUND
            );
        }
    }

}
