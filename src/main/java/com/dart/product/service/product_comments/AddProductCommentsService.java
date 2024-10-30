package com.dart.product.service.product_comments;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_comments_model.AddProductCommentReqModel;
import com.dart.product.entity.product_comments_model.ProductCommentOneResModel;
import com.dart.product.entity.product_comments_model.SaveAndUpdateProductCommentResponse;
import com.dart.product.entity.product_reviews_model.SaveAndUpdateProductReviewResponse;
import com.dart.product.utilities.AppConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AddProductCommentsService {

    private final ServiceLocator serviceLocator;

    public AddProductCommentsService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<ProductCommentOneResModel> addProductComment(
            String token, AddProductCommentReqModel reqBody) {

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
        SaveAndUpdateProductCommentResponse saveRecordInDb = serviceLocator
                .getSaveAndUpdateRecord()
                .saveProductComment(serviceLocator.getProductMappers().mapAddProductCommentModelToDbModel(reqBody));


        return null;

    }


    private void validateRequestBody(AddProductCommentReqModel reqBody) {
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

