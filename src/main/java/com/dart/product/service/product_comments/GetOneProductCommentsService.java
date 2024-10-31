package com.dart.product.service.product_comments;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_comments_model.FetchOneProductCommentModel;
import com.dart.product.entity.product_comments_model.ProductCommentDbModel;
import com.dart.product.entity.product_comments_model.ProductCommentOneResModel;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class GetOneProductCommentsService {

    private final ServiceLocator serviceLocator;

    public GetOneProductCommentsService(ServiceLocator serviceLocator) {
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

        FetchOneProductCommentModel fetchOneProductCommentFromCache = serviceLocator.getRedisProductCacheRepo().findOneProductComment(organisationId.toString(), productId, id);

        if(fetchOneProductCommentFromCache.getStatus()){
            return new ResponseEntity<>(serviceLocator.getProductMappers().productCommentResponseBuilder(serviceLocator.getProductMappers().mapDbModelToProductCommentDbModel(fetchOneProductCommentFromCache.getProductComment()), "product comment successfully fetch"), HttpStatus.OK);
        }else{
            ProductCommentDbModel isProductCommentExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);
            return new ResponseEntity<>(serviceLocator.getProductMappers().productCommentResponseBuilder(isProductCommentExistingInDb, "product comment successfully fetch"), HttpStatus.OK);

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

    private ProductCommentDbModel findByIdAndOrganisationIdAndIsActive(Integer id, Integer productId, UUID organisationId) {
        return serviceLocator.getProductCommentRepo().findByIdAndProductIdAndOrganisationIdAndIsActive(id,  productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}

