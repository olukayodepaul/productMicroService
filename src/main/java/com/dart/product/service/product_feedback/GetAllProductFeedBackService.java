package com.dart.product.service.product_feedback;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_feedback.*;
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
public class GetAllProductFeedBackService {

    private final ServiceLocator serviceLocator;

    public GetAllProductFeedBackService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<ProductFeedBackAllResModel> addProductFeedBack(
            String token, Integer productId, Integer id) {

        validateRequestToken(token);


        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        FetchAllProductFeedBackModel fetchAllProductFeedBackFromCache = serviceLocator.getRedisProductCacheRepo().findAllProductFeedBack(organisationId.toString(), productId);

        if(fetchAllProductFeedBackFromCache.getStatus()){
            List<ProductFeedBackDbModel> allFeedBack = serviceLocator.getProductMappers().mapToAllProductFeedBack(fetchAllProductFeedBackFromCache.getProductFeedBack());
            return new ResponseEntity<>(serviceLocator.getProductMappers().allProductFeedBackResponseBuilder(allFeedBack, "product comment successfully fetch"), HttpStatus.OK);
        }

        List<ProductFeedBackDbModel> allFeedBack  = findByIdAndOrganisationIdAndIsActive(productId, organisationId);
        return new ResponseEntity<>(serviceLocator.getProductMappers().allProductFeedBackResponseBuilder(allFeedBack, "product comment successfully fetch"), HttpStatus.OK);

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

    private List<ProductFeedBackDbModel> findByIdAndOrganisationIdAndIsActive(Integer productId, UUID organisationId) {
        return serviceLocator.getProductFeedBackRepo().findByProductIdAndOrganisationIdAndIsActive( productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }


}
