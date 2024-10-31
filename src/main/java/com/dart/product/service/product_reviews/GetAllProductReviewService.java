package com.dart.product.service.product_reviews;


import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_policy_model.FetchAllProductPolicyModel;
import com.dart.product.entity.product_policy_model.ProductPolicyCacheModel;
import com.dart.product.entity.product_reviews_model.*;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetAllProductReviewService {


    private final ServiceLocator serviceLocator;

    public GetAllProductReviewService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<ProductReviewAllResModel> getOneProductPolicy(
            String token,  Integer productId, Integer id) {

        validateRequestToken(token);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        FetchAllProductReviewModel fetchAllProductReviewFromCache = serviceLocator.getRedisProductCacheRepo().findAllProductReview(organisationId.toString(), productId);

        if(fetchAllProductReviewFromCache.getStatus()) {
            List<ProductReviewDbModel> allProductReview = serviceLocator.getProductMappers().mapToAllProductReview(fetchAllProductReviewFromCache.getProductReview());
            return new ResponseEntity<>(serviceLocator.getProductMappers().allProductReviewResponseBuilder(allProductReview, "fetch all product review"), HttpStatus.OK);
        }else{
            List<ProductReviewDbModel> allProductReview =  findByIdAndOrganisationIdAndIsActive(productId, organisationId);
            return new ResponseEntity<>(serviceLocator.getProductMappers().allProductReviewResponseBuilder(allProductReview, "fetch all product review"), HttpStatus.OK);
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

    private List<ProductReviewDbModel> findByIdAndOrganisationIdAndIsActive( Integer productId, UUID organisationId) {
        return serviceLocator.getProductReviewRepo().findByProductIdAndOrganisationIdAndIsActive(productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}
