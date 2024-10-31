package com.dart.product.service.related_products;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.related_products_model.AddRelatedProductsReqModel;
import com.dart.product.entity.related_products_model.RelatedProductsDbModel;
import com.dart.product.entity.related_products_model.RelatedProductsOneResModel;
import com.dart.product.entity.related_products_model.SaveAndUpdateRelatedProductResponse;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeleteRelatedProductsService {

    private final ServiceLocator serviceLocator;

    public DeleteRelatedProductsService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<RelatedProductsOneResModel> addProductPolicy(
            String token, Integer productId, Integer id) {

        validateRequestToken(token);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        RelatedProductsDbModel isRelatedProductExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);

        isRelatedProductExistingInDb.setUpdatedAt(LocalDateTime.now());
        isRelatedProductExistingInDb.setActive(false);
        SaveAndUpdateRelatedProductResponse updatedRecordInDb = serviceLocator
                .getSaveAndUpdateRecord()
                .saveRelatedProduct(isRelatedProductExistingInDb);


        isRecordSaveInTheDb(updatedRecordInDb);
        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo().deleteRelatedProduct(serviceLocator.getProductMappers().mapRelatedProductsCacheModelToDbModel(updatedRecordInDb.getRelatedProducts()));

        isRecordSaveInTheCache(cacheRecordInMemory);

        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
        return new ResponseEntity<>(serviceLocator.getProductMappers().relatedProductsOneResponseBuilder(updatedRecordInDb.getRelatedProducts(), "related  product successfully deleted"), HttpStatus.OK);

    }

    private void isRecordSaveInTheCache(boolean isRecord) {
        if(!isRecord){
            //send through kafka
        }
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

    private void isRecordSaveInTheDb(SaveAndUpdateRelatedProductResponse isSave) {
        if(!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private RelatedProductsDbModel findByIdAndOrganisationIdAndIsActive(Integer id, Integer productId, UUID organisationId) {
        return serviceLocator.getRelatedProductsDbModel().findByIdAndProductIdAndOrganisationIdAndIsActive(id,  productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }


}
