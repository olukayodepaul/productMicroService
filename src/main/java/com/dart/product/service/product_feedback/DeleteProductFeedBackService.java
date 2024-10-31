package com.dart.product.service.product_feedback;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_feedback.AddProductFeedBackReqModel;
import com.dart.product.entity.product_feedback.ProductFeedBackDbModel;
import com.dart.product.entity.product_feedback.ProductFeedBackOneResModel;
import com.dart.product.entity.product_feedback.SaveAndUpdateProductFeedBackResponse;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeleteProductFeedBackService {

    private final ServiceLocator serviceLocator;

    public DeleteProductFeedBackService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<ProductFeedBackOneResModel> addProductFeedBack(
            String token, Integer productId, Integer id) {

        validateRequestToken(token);


        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        ProductFeedBackDbModel isProductFeedBackExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);


        isProductFeedBackExistingInDb.setUpdatedAt(LocalDateTime.now());
        isProductFeedBackExistingInDb.setActive(false);
        SaveAndUpdateProductFeedBackResponse saveRecordInDb = serviceLocator
                .getSaveAndUpdateRecord()
                .saveProductFeedBack(isProductFeedBackExistingInDb);

        isRecordSaveInTheDb(saveRecordInDb);
        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo().deleteProductFeedBack(serviceLocator.getProductMappers().mapProductFeedBackDbModelToDbModel(saveRecordInDb.getProductFeedback()));

        isRecordSaveInTheCache(cacheRecordInMemory);

        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
        return new ResponseEntity<>(serviceLocator.getProductMappers().productFeedBackResponseBuilder(saveRecordInDb.getProductFeedback(), "product comment successfully deleted"), HttpStatus.OK);

    }

    private void isRecordSaveInTheCache(boolean isRecord) {
        if(!isRecord){
            //send through kafka
        }
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

    private void isRecordSaveInTheDb(SaveAndUpdateProductFeedBackResponse isSave) {
        if(!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private ProductFeedBackDbModel findByIdAndOrganisationIdAndIsActive(Integer id, Integer productId, UUID organisationId) {
        return serviceLocator.getProductFeedBackRepo().findByIdAndProductIdAndOrganisationIdAndIsActive(id,  productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}
