package com.dart.product.service.product_specification;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_specification_model.AddProductSpecReqModel;
import com.dart.product.entity.product_specification_model.AddProductSpecResModel;
import com.dart.product.entity.product_specification_model.SaveAndUpdateProductSpecResponse;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AddProductSpecService {

    private final ServiceLocator serviceLocator;

    public AddProductSpecService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<AddProductSpecResModel> addProductSpec(AddProductSpecReqModel reqBody, String token) {

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
        SaveAndUpdateProductSpecResponse saveRecordInDb = serviceLocator
                .getSaveAndUpdateRecord()
                .saveProductSpecification(serviceLocator.getProductMappers().mapProductSpec(reqBody));

        isRecordSaveInTheDb(saveRecordInDb);

        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo()
                .saveUpdateProductSpec(serviceLocator.getProductMappers().mapProductSpecToCache(saveRecordInDb.getProductSpec()));

        isRecordSaveInTheCache(cacheRecordInMemory);

        //todo: send newly created product specification to searchMicroService (grpc)

        return new ResponseEntity<>(serviceLocator.getProductMappers().productsSpecResponse(saveRecordInDb.getProductSpec(), "product specification successfully created"), HttpStatus.CREATED);
    }

    private void isRecordSaveInTheDb(SaveAndUpdateProductSpecResponse isSave) {
        if(!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void isRecordSaveInTheCache(boolean isRecord) {
        if(!isRecord){
            //send through kafka
        }
    }

    private void validateRequestBody(AddProductSpecReqModel reqBody) {
        serviceLocator.getValidationUtils().productSpecValidate(reqBody);
    }

    private void validateRequestToken(String token) {
        serviceLocator.getValidationUtils().jwtValidateRequest(token);
    }

    private void validateBruteForceProtection(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private void validationUserRole(String role){
        serviceLocator.getValidationUtils().roleValidation(role);
    }

}
