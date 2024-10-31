package com.dart.product.service.product_tag;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_tags_model.AddProductTagReqModel;
import com.dart.product.entity.product_tags_model.ProductTagDbModel;
import com.dart.product.entity.product_tags_model.ProductTagOneResModel;
import com.dart.product.entity.product_tags_model.SaveAndUpdateProductTagResponse;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeleteProductTagService {

    private final ServiceLocator serviceLocator;

    public DeleteProductTagService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<ProductTagOneResModel> updateProductPolicy(
            String token, Integer productId, Integer id) {

        validateRequestToken(token);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        ProductTagDbModel isProductTagExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);

        isProductTagExistingInDb.setUpdatedAt(LocalDateTime.now());
        isProductTagExistingInDb.setActive(false);
        SaveAndUpdateProductTagResponse updateRecordInDb = serviceLocator
                .getSaveAndUpdateRecord()
                .saveProductTag(isProductTagExistingInDb);


        isRecordDeletedInTheDb(updateRecordInDb);
        boolean deleteCacheRecordInMemory = serviceLocator.getRedisProductCacheRepo().deleteProductTag(serviceLocator.getProductMappers().mapProductTagDbModelToDbModel(updateRecordInDb.getProductTags()));

        isRecordDeletedInTheCache(deleteCacheRecordInMemory);

        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
        return new ResponseEntity<>(serviceLocator.getProductMappers().productTagResponseBuilder(updateRecordInDb.getProductTags(), "product tag successfully updated"), HttpStatus.OK);

    }

    private void isRecordDeletedInTheCache(boolean isRecord) {
        if(!isRecord){
            //send through kafka
        }
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

    private void isRecordDeletedInTheDb(SaveAndUpdateProductTagResponse isSave) {
        if(!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private ProductTagDbModel findByIdAndOrganisationIdAndIsActive(Integer id, Integer productId, UUID organisationId) {
        return serviceLocator.getProductTagRepo().findByIdAndProductIdAndOrganisationIdAndIsActive(id,  productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }




}
