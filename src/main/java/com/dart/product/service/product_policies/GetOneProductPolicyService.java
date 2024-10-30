package com.dart.product.service.product_policies;


import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_policy_model.FetchOnelProductPolicyModel;
import com.dart.product.entity.product_policy_model.ProductPolicyDbModel;
import com.dart.product.entity.product_policy_model.ProductPolicyOneResModel;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class GetOneProductPolicyService {

    private final ServiceLocator serviceLocator;

    public GetOneProductPolicyService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<ProductPolicyOneResModel> getOneProductPolicy(String token, Integer id, Integer productId) {

        validateRequestToken(token);
        //validateProductId(productId);
        //validateId(Id);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        FetchOnelProductPolicyModel fetchOneProductPolicyFromCache = serviceLocator.getRedisProductCacheRepo().findOneProductPolicy(organisationId.toString(), productId, id);

        if(fetchOneProductPolicyFromCache.getStatus()) {
            return new ResponseEntity<>(serviceLocator.getProductMappers().productPolicyOneResponseBuilder(serviceLocator.getProductMappers().mapCacheToProductPolicy(fetchOneProductPolicyFromCache.getProductPolicy()), "product policy successfully fetch"), HttpStatus.OK);
        }else{
            ProductPolicyDbModel isProductPolicyExistingInDb = findByIdAndOrganisationIdAndIsActive(id, organisationId, productId);
            return new ResponseEntity<>(serviceLocator.getProductMappers().productPolicyOneResponseBuilder(isProductPolicyExistingInDb, "product policy successfully fetch"), HttpStatus.OK);
        }
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

    private ProductPolicyDbModel findByIdAndOrganisationIdAndIsActive(Integer id, UUID organisationId, Integer ProductId) {
        return serviceLocator.getProductPolicyRepo().findByIdAndOrganisationIdAndIsActiveAndProductId(id, organisationId, true, ProductId)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}
