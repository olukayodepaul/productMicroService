package com.dart.product.service.product_comments;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_comments_model.*;
import com.dart.product.entity.product_tags_model.ProductTagDbModel;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetAllProductCommentsService {

    private final ServiceLocator serviceLocator;

    public GetAllProductCommentsService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<ProductCommentAllResModel> addProductComment(
            String token,  Integer productId, Integer id) {

        validateRequestToken(token);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        FetchAllProductCommentModel fetchAllProductCommentFromCache = serviceLocator.getRedisProductCacheRepo().findAllProductComment(organisationId.toString(), productId);

        if(fetchAllProductCommentFromCache.getStatus()){
            List<ProductCommentDbModel> allProductComment = serviceLocator.getProductMappers().mapToAllProductComment(fetchAllProductCommentFromCache.getProductComment());
            return new ResponseEntity<>(serviceLocator.getProductMappers().allProductCommentResponseBuilder(allProductComment, "product comment successfully fetch"), HttpStatus.OK);
        }

        List<ProductCommentDbModel> allProductComment = findByIdAndOrganisationIdAndIsActive(productId, organisationId);
        return new ResponseEntity<>(serviceLocator.getProductMappers().allProductCommentResponseBuilder(allProductComment, "product comment successfully fetch"), HttpStatus.OK);

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

    private List<ProductCommentDbModel> findByIdAndOrganisationIdAndIsActive(Integer productId, UUID organisationId) {
        return serviceLocator.getProductCommentRepo().findByProductIdAndOrganisationIdAndIsActive( productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}

