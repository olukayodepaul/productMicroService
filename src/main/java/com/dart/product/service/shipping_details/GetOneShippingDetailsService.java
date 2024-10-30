package com.dart.product.service.shipping_details;


import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.shipping_details_model.FetchOnelShippingDetailsModel;
import com.dart.product.entity.shipping_details_model.ShippingDetailsDbModel;
import com.dart.product.entity.shipping_details_model.ShippingDetailsOneResModel;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetOneShippingDetailsService {

    private final ServiceLocator serviceLocator;

    public GetOneShippingDetailsService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<ShippingDetailsOneResModel> getOneShippingDetails(String token, Integer id, Integer productId) {

        validateRequestToken(token);
        //validateProductId(productId);
        // validateId(Id);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        FetchOnelShippingDetailsModel fetchOneShippingDetailFromCache = serviceLocator.getRedisProductCacheRepo().findOneShippingDetails(organisationId.toString(), productId, id);

        if(fetchOneShippingDetailFromCache.getStatus()){

            return new ResponseEntity<>(serviceLocator.getProductMappers().shippingDetailsOneResponseBuilder
                    (serviceLocator.getProductMappers().mapCacheModelToDbModel(fetchOneShippingDetailFromCache.getShippingDetails()),"Shipping details successfully fetched"), HttpStatus.CREATED);

        }else{

            ShippingDetailsDbModel getOneShippingDetailFromDb = findByIdAndOrganisationIdAndProductIdAndIsActive(id, organisationId, productId);
            return new ResponseEntity<>(serviceLocator.getProductMappers().shippingDetailsOneResponseBuilder
                    (getOneShippingDetailFromDb,"Shipping details successfully fetched"), HttpStatus.CREATED);
        }
    }

    private void validateRequestToken(String token) {
        serviceLocator.getValidationUtils().jwtValidateRequest(token);
    }

    private void validationUserRole(String role){
        serviceLocator.getValidationUtils().roleValidation(role);
    }

    private void validateBruteForceProtection(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private ShippingDetailsDbModel findByIdAndOrganisationIdAndProductIdAndIsActive(Integer id, UUID organisationId, Integer productId) {
        return serviceLocator.getShippingDetailsRepo().findByIdAndOrganisationIdAndProductIdAndIsActive(id, organisationId,  productId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}
