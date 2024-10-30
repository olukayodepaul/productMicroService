package com.dart.product.service.product_tag;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_tags_model.AddProductTagReqModel;
import com.dart.product.entity.product_tags_model.ProductTagOneResModel;
import com.dart.product.entity.product_tags_model.SaveAndUpdateProductTagResponse;
import com.dart.product.entity.special_offers_model.AddSpecialOffersReqModel;
import com.dart.product.entity.special_offers_model.SaveAndUpdateSpecialOffersResponse;
import com.dart.product.utilities.AppConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AddProductTagService {

    private final ServiceLocator serviceLocator;

    public AddProductTagService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<ProductTagOneResModel> addProductPolicy(
            String token, AddProductTagReqModel reqBody) {

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
        SaveAndUpdateProductTagResponse saveRecordInDb = serviceLocator
                .getSaveAndUpdateRecord()
                .saveProductTag(serviceLocator.getProductMappers().mapAddProductTagModelToDbModel(reqBody));



        return null;

    }

    private void validateRequestBody(AddProductTagReqModel reqBody) {
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
