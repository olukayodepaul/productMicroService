package com.dart.product.service.product_feedback;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_feedback.AddProductFeedBackReqModel;
import com.dart.product.entity.product_feedback.ProductFeedBackOneResModel;
import com.dart.product.entity.product_feedback.SaveAndUpdateProductFeedBackResponse;
import com.dart.product.utilities.AppConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AddProductFeedBackService {

    private final ServiceLocator serviceLocator;

    public AddProductFeedBackService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<ProductFeedBackOneResModel> addProductFeedBack(
            String token, AddProductFeedBackReqModel reqBody) {

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
        SaveAndUpdateProductFeedBackResponse saveRecordInDb = serviceLocator
                .getSaveAndUpdateRecord()
                .saveProductFeedBack(serviceLocator.getProductMappers().mapAddProductFeedBackModelToDbModel(reqBody));
        return null;

    }

    private void validateRequestBody(AddProductFeedBackReqModel reqBody) {
        //do the validation
        //serviceLocator.getValidationUtils().productSpecValidate(reqBody);
    }

    private void validateRequestToken(String token) {
        serviceLocator.getValidationUtils().jwtValidateRequest(token);
    }

    private void validationUserRole(String role){
        serviceLocator.getValidationUtils().customerRoleValidation(role);
    }

    private void validateBruteForceProtection(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }
}
