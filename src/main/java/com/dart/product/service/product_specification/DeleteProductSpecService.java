package com.dart.product.service.product_specification;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_specification_model.AddProductSpecResModel;
import com.dart.product.entity.product_specification_model.ProductSpecificationDbModel;
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
public class DeleteProductSpecService {


    private final ServiceLocator serviceLocator;

    public DeleteProductSpecService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }


    public ResponseEntity<AddProductSpecResModel> updateProductSpec(Integer id, String token) {

        validateRequestToken(token);
        validationUserId(id);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        ProductSpecificationDbModel isProductSpecExistingInDb =  findByIdAndOrganisationIdAndIsActive(id, organisationId);

        isProductSpecExistingInDb.setActive(false);
        isProductSpecExistingInDb.setUpdatedAt(LocalDateTime.now());
        SaveAndUpdateProductSpecResponse deleteRecordInDb = serviceLocator
                .getSaveAndUpdateRecord()
                .saveProductSpecification(serviceLocator.getProductMappers().mapProductSpecToProductSpec(isProductSpecExistingInDb));

        validateIfRecordIsDeleted(deleteRecordInDb);

        boolean deleteCacheRecord = serviceLocator.getRedisProductCacheRepo()
                .deleteProductSpec(serviceLocator.getProductMappers().mapProductSpecToCache(deleteRecordInDb.getProductSpec()));

        validateIfCacheRecordDeleted(deleteCacheRecord);

        return new ResponseEntity<>(serviceLocator.getProductMappers().productsSpecResponse(deleteRecordInDb.getProductSpec(), "product specification successfully deleted"), HttpStatus.OK);

    }

    private void validateIfCacheRecordDeleted(boolean isRecord) {
        if(!isRecord){
            //send through kafka
        }
    }

    private void validateRequestToken (String token){
        serviceLocator.getValidationUtils().jwtValidateRequest(token);
    }

    private void validateBruteForceProtection(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private void validationUserRole(String role){
        serviceLocator.getValidationUtils().roleValidation(role);
    }

    private void validationUserId(Integer id){
        serviceLocator.getValidationUtils().productSpecValidation(id);
    }

    private ProductSpecificationDbModel findByIdAndOrganisationIdAndIsActive(Integer id, UUID organisationId) {
        return serviceLocator.getProductSpecificationRepo().findByIdAndOrganisationIdAndIsActive(id, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    private void validateIfRecordIsDeleted(SaveAndUpdateProductSpecResponse isSave) {
        if(!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
