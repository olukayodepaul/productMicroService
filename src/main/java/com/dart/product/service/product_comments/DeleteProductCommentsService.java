package com.dart.product.service.product_comments;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_comments_model.ProductCommentDbModel;
import com.dart.product.entity.product_comments_model.ProductCommentOneResModel;
import com.dart.product.entity.product_comments_model.SaveAndUpdateProductCommentResponse;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeleteProductCommentsService {

    private final ServiceLocator serviceLocator;

    public DeleteProductCommentsService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<ProductCommentOneResModel> addProductComment(
            String token,  Integer productId, Integer id) {

        validateRequestToken(token);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        ProductCommentDbModel isProductCommentExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);

        isProductCommentExistingInDb.setUpdatedAt(LocalDateTime.now());
        isProductCommentExistingInDb.setActive(false);
        SaveAndUpdateProductCommentResponse updateRecordInDb = serviceLocator
                .getSaveAndUpdateRecord()
                .saveProductComment(isProductCommentExistingInDb);
        isRecordDeletedInTheDb(updateRecordInDb);

        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo().deleteProductComment(serviceLocator.getProductMappers().mapProductCommentDbModelToDbModel(updateRecordInDb.getProductComments()));
        isRecordDeletedInTheCache(cacheRecordInMemory);

        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
        return new ResponseEntity<>(serviceLocator.getProductMappers().productCommentResponseBuilder(updateRecordInDb.getProductComments(), "product comment successfully deleted"), HttpStatus.OK);

    }

    private void isRecordDeletedInTheCache(boolean isRecord) {
        if(!isRecord){
            //send through kafka
        }
    }

    private void validateRequestToken(String token) {
        serviceLocator.getValidationUtils().jwtValidateRequest(token);
    }

    private void validationUserRole(String role) {
        serviceLocator.getValidationUtils().customerRoleValidation(role);
    }

    private void validateBruteForceProtection(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private void isRecordDeletedInTheDb(SaveAndUpdateProductCommentResponse isSave) {
        if(!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private ProductCommentDbModel findByIdAndOrganisationIdAndIsActive(Integer id, Integer productId, UUID organisationId) {
        return serviceLocator.getProductCommentRepo().findByIdAndProductIdAndOrganisationIdAndIsActive(id,  productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}

