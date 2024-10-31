package com.dart.product.service.product_feedback;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_feedback.FetchOneProductFeedBackModel;
import com.dart.product.entity.product_feedback.ProductFeedBackDbModel;
import com.dart.product.entity.product_feedback.ProductFeedBackOneResModel;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class GetOneProductFeedBackService {

    private final ServiceLocator serviceLocator;

    public GetOneProductFeedBackService(ServiceLocator serviceLocator) {
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

        FetchOneProductFeedBackModel fetchOneProductFeedBackFromCache = serviceLocator.getRedisProductCacheRepo().findOneProductFeedBack(organisationId.toString(), productId, id);

        if(fetchOneProductFeedBackFromCache.getStatus()){
            return new ResponseEntity<>(serviceLocator.getProductMappers().productFeedBackResponseBuilder(serviceLocator.getProductMappers().mapDbModelToProductFeedBackDbModel(fetchOneProductFeedBackFromCache.getProductFeedBack()), "product comment successfully fetch"), HttpStatus.OK);
        }else{
            ProductFeedBackDbModel isProductFeedBackExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);
            return new ResponseEntity<>(serviceLocator.getProductMappers().productFeedBackResponseBuilder(isProductFeedBackExistingInDb, "product comment successfully fetch"), HttpStatus.OK);
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

    private ProductFeedBackDbModel findByIdAndOrganisationIdAndIsActive(Integer id, Integer productId, UUID organisationId) {
        return serviceLocator.getProductFeedBackRepo().findByIdAndProductIdAndOrganisationIdAndIsActive(id,  productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }


}
