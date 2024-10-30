package com.dart.product.service.shipping_details;


import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.shipping_details_model.AddShippingDetailsReqModel;
import com.dart.product.entity.shipping_details_model.SaveAndUpdateShippingDetailsResponse;
import com.dart.product.entity.shipping_details_model.ShippingDetailsDbModel;
import com.dart.product.entity.shipping_details_model.ShippingDetailsOneResModel;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class DeleteShippingDetailsService {

    private final ServiceLocator serviceLocator;

    public DeleteShippingDetailsService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<ShippingDetailsOneResModel> deleteShippingDetails(String token, Integer productId, Integer id)
    {

        validateRequestToken(token);
//        validateProductId(productId);
//        validateId(Id);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        ShippingDetailsDbModel isShippingDetailsExistingInDb = findByIdAndOrganisationIdAndIsActive(id, organisationId, productId);

        isShippingDetailsExistingInDb.setActive(false);
        isShippingDetailsExistingInDb.setUpdatedAt(LocalDateTime.now());
        SaveAndUpdateShippingDetailsResponse deleteRecordInDb = serviceLocator.getSaveAndUpdateRecord().saveShippingDetails(isShippingDetailsExistingInDb);

        validateIfDbRecordIsDeleted(deleteRecordInDb);

        boolean deleteCacheRecord = serviceLocator.getRedisProductCacheRepo()
                .deleteShippingDetails(serviceLocator.getProductMappers().mapShippingDetailsCacheModelToDbModel(deleteRecordInDb.getShippingDetails()));


        validateIfCacheRecordIsDeleted(deleteCacheRecord);

        return new ResponseEntity<>(serviceLocator.getProductMappers().shippingDetailsOneResponseBuilder
                (deleteRecordInDb.getShippingDetails(),"Shipping details successfully deleted"), HttpStatus.OK);

    }

    private void validateIfCacheRecordIsDeleted(boolean isRecord) {
        if(!isRecord){
            //send through kafka
        }
    }

    private void validateIfDbRecordIsDeleted(SaveAndUpdateShippingDetailsResponse isSave) {
        if(!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateRequestToken(String token) {
        serviceLocator.getValidationUtils().jwtValidateRequest(token);
    }

    private void validateRequestBody(AddShippingDetailsReqModel reqBody) {
        serviceLocator.getValidationUtils().shippingDetailsValidation(reqBody);
    }

    private void validationUserRole(String role){
        serviceLocator.getValidationUtils().roleValidation(role);
    }

    private void validateBruteForceProtection(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private ShippingDetailsDbModel findByIdAndOrganisationIdAndIsActive(Integer id, UUID organisationId, Integer productId) {
        return serviceLocator.getShippingDetailsRepo().findByIdAndOrganisationIdAndProductIdAndIsActive(id, organisationId, productId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }


}
