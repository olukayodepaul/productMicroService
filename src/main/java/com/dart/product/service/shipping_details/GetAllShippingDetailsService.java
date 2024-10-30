package com.dart.product.service.shipping_details;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.shipping_details_model.FetchAllShippingDetailsModel;
import com.dart.product.entity.shipping_details_model.ShippingDetailsAllResModel;
import com.dart.product.entity.shipping_details_model.ShippingDetailsDbModel;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class GetAllShippingDetailsService {


    private final ServiceLocator serviceLocator;

    public GetAllShippingDetailsService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }


    public ResponseEntity<ShippingDetailsAllResModel> getAllShippingDetails(String token, Integer productId) {

        validateRequestToken(token);
        validateProductId(productId);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        FetchAllShippingDetailsModel fetchAllShippingDetailsFromCache = serviceLocator.getRedisProductCacheRepo().findAllShippingDetails(organisationId.toString(), productId);

        if(fetchAllShippingDetailsFromCache.getStatus()) {

            List<ShippingDetailsDbModel> allShippingDetails = serviceLocator.getProductMappers().mapAllCacheToDbModel(fetchAllShippingDetailsFromCache.getShippingDetails());
            return new ResponseEntity<>(serviceLocator.getProductMappers().shippingDetailsAllResponseBuilder(allShippingDetails,""), HttpStatus.OK);

        }else{

            List<ShippingDetailsDbModel> getAllShippingDetails = findByOrganisationIdAndProductIdAndIsActive(organisationId, productId);
            validateIfShippingDetailsExists(getAllShippingDetails);
            return new ResponseEntity<>(serviceLocator.getProductMappers().shippingDetailsAllResponseBuilder(getAllShippingDetails,""), HttpStatus.OK);
        }

    }

    private List<ShippingDetailsDbModel> findByOrganisationIdAndProductIdAndIsActive(UUID organisationId, Integer productId) {

        return serviceLocator.getShippingDetailsRepo().findByOrganisationIdAndProductIdAndIsActive(organisationId, productId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }


    private void validateRequestToken(String token) {
        serviceLocator.getValidationUtils().jwtValidateRequest(token);
    }

    private void validateProductId(Integer productId) {
        serviceLocator.getValidationUtils().productIdValidation(productId);
    }

    private void validationUserRole(String role){
        serviceLocator.getValidationUtils().roleValidation(role);
    }

    private void validateBruteForceProtection(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private void validateIfShippingDetailsExists(List<ShippingDetailsDbModel> productSpec) {
        if (productSpec.isEmpty()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, HttpStatus.NOT_FOUND.toString(), AppConfig.FETCH_ALL_PRODUCT_MEDIA),
                    HttpStatus.NOT_FOUND
            );
        }
    }


}
