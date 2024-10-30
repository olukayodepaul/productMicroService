package com.dart.product.service.product_policies;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_policy_model.AddProductPolicyReqModel;
import com.dart.product.entity.product_policy_model.ProductPolicyOneResModel;
import com.dart.product.entity.product_policy_model.SaveAndUpdateProductPolicyResponse;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class AddProductPolicyService {

    private final ServiceLocator serviceLocator;

    public AddProductPolicyService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<ProductPolicyOneResModel> addProductPolicy(
            String token, AddProductPolicyReqModel reqBody) {

        validateRequestToken(token);
        validateRequestBody(reqBody);


        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        //allow only one product policies to be created for a productId once. it can be updated, deleted and create another one.
        findByIsActiveAndProductIdAndOrganisationId(organisationId, reqBody.getProduct_id());

        reqBody.setOrganisation_id(organisationId);
        reqBody.setCreated_at(LocalDateTime.now());
        reqBody.setUpdated_at(LocalDateTime.now());
        reqBody.set_active(true);
        reqBody.setId(0);
        SaveAndUpdateProductPolicyResponse saveRecordInDb = serviceLocator
                .getSaveAndUpdateRecord()
                .saveProductPolicy(serviceLocator.getProductMappers().mapAddProductPolicyReqModelToDbModel(reqBody));

        isRecordSaveInTheDb(saveRecordInDb);

        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo()
                .saveUpdateProductPolicy(serviceLocator.getProductMappers().mapProductPolicyToCache(saveRecordInDb.getProductPolicy()));

        isRecordSaveInTheCache(cacheRecordInMemory);

        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer

        return new ResponseEntity<>(serviceLocator.getProductMappers().productPolicyOneResponseBuilder(saveRecordInDb.getProductPolicy(), "product policy successfully created"), HttpStatus.CREATED);
    }

    private void isRecordSaveInTheCache(boolean isRecord) {
        if(!isRecord){
            //send through kafka
        }
    }

    private void isRecordSaveInTheDb(SaveAndUpdateProductPolicyResponse isSave) {
        if(!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateRequestBody(AddProductPolicyReqModel reqBody) {
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

    /**
     *
     * @param organisationId
     * @param ProductId
     */
    private void findByIsActiveAndProductIdAndOrganisationId(UUID organisationId, Integer ProductId) {
        if(serviceLocator.getProductPolicyRepo().findByIsActiveAndProductIdAndOrganisationId(true, ProductId, organisationId).isPresent()){
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), "error"),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
