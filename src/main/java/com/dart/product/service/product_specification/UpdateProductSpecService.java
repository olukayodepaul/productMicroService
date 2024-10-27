package com.dart.product.service.product_specification;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_specification_model.AddProductSpecReqModel;
import com.dart.product.entity.product_specification_model.AddProductSpecResModel;
import com.dart.product.entity.product_specification_model.ProductSpecificationDbModel;
import com.dart.product.entity.product_specification_model.SaveAndUpdateProductSpecResponse;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UpdateProductSpecService {

    private final ServiceLocator serviceLocator;

    public UpdateProductSpecService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<AddProductSpecResModel> updateProductSpec(AddProductSpecReqModel reqBody, String token, Integer id) {

        validateRequestToken(token);
        validateRequestBody(reqBody);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);


        ProductSpecificationDbModel isProductSpecExistingInDb =  findByIdAndOrganisationIdAndIsActive(id, organisationId);

        reqBody.setOrganisation_id(isProductSpecExistingInDb.getOrganisationId());
        reqBody.setCreated_at(isProductSpecExistingInDb.getCreatedAt());
        reqBody.set_active(isProductSpecExistingInDb.isActive());
        reqBody.setId(isProductSpecExistingInDb.getId());
        SaveAndUpdateProductSpecResponse updateRecordInDb = serviceLocator
                .getSaveAndUpdateRecord()
                .saveProductSpecification(serviceLocator.getProductMappers().mapProductSpec(reqBody));

        validateIfRecordIsUpdated(updateRecordInDb);

        boolean cacheRecord = serviceLocator.getRedisProductCacheRepo()
                .saveUpdateProductSpec(serviceLocator.getProductMappers().mapProductSpecToCache(updateRecordInDb.getProductSpec()));

        validateIfRecordIsCache(cacheRecord);

        return new ResponseEntity<>(serviceLocator.getProductMappers().productsSpecResponse(updateRecordInDb.getProductSpec(), "product specification successfully updated"), HttpStatus.OK);

    }

    private void validateIfRecordIsCache(boolean isRecord) {
        if(!isRecord){
            //send through kafka
        }
    }

    private void validateIfRecordIsUpdated(SaveAndUpdateProductSpecResponse isSave) {
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

    private void validateRequestBody(AddProductSpecReqModel reqBody) {
        serviceLocator.getValidationUtils().productSpecValidate(reqBody);
    }

    private void validateBruteForceProtection(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private void validationUserRole(String role){
        serviceLocator.getValidationUtils().roleValidation(role);
    }

    private ProductSpecificationDbModel findByIdAndOrganisationIdAndIsActive(Integer id, UUID organisationId) {
        return serviceLocator.getProductSpecificationRepo().findByIdAndOrganisationIdAndIsActive(id, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}
