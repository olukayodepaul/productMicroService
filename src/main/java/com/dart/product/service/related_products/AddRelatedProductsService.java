package com.dart.product.service.related_products;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.related_products_model.AddRelatedProductsReqModel;
import com.dart.product.entity.related_products_model.RelatedProductsOneResModel;
import com.dart.product.entity.related_products_model.SaveAndUpdateRelatedProductResponse;
import com.dart.product.utilities.AppConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AddRelatedProductsService {

    private final ServiceLocator serviceLocator;

    public AddRelatedProductsService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<RelatedProductsOneResModel> addProductPolicy(
            String token, AddRelatedProductsReqModel reqBody) {


        validateRequestToken(token);
        validateRequestBody(reqBody);


        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);


        reqBody.setOrganisation_id(organisationId);
        reqBody.setCreated_at(LocalDateTime.now());
        reqBody.setUpdated_at(LocalDateTime.now());
        reqBody.set_active(true);
        reqBody.setId(0);
        SaveAndUpdateRelatedProductResponse saveRecordInDb = serviceLocator
                .getSaveAndUpdateRecord()
                .saveRelatedProduct(serviceLocator.getProductMappers().mapAddRelatedProductModelToDbModel(reqBody));


        return null;

    }

    private void validateRequestBody(AddRelatedProductsReqModel reqBody) {
        //do the validation
        //serviceLocator.getValidationUtils().productSpecValidate(reqBody);
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

}
