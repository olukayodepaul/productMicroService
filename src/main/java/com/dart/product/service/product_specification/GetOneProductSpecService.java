package com.dart.product.service.product_specification;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_specification_model.AddProductSpecResModel;
import com.dart.product.entity.product_specification_model.FetchOnelProductSpecModel;
import com.dart.product.entity.product_specification_model.ProductSpecificationDbModel;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class GetOneProductSpecService {

    private final ServiceLocator serviceLocator;

    public GetOneProductSpecService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<AddProductSpecResModel> getOneProductSpec(Integer id, Integer productId, String token) {

        validateRequestToken(token);
        validateProductId(productId);
        validateRequestMediaId(id);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        FetchOnelProductSpecModel fetchOneProductSpecFromCache = serviceLocator.getRedisProductCacheRepo().findOneProductSpec(organisationId.toString(), productId, id);

        if(fetchOneProductSpecFromCache.getStatus()) {
            return new ResponseEntity<>(serviceLocator.getProductMappers().productsSpecResponse(serviceLocator.getProductMappers().mapCacheToProductSpec(fetchOneProductSpecFromCache.getProductSpec()), "product specification successfully fetch"), HttpStatus.OK);
        }else{
            ProductSpecificationDbModel getOneProductSpecFromDb = findByIdAndOrganisationIdAndIsActiveAndProductId(id, organisationId, productId);
            return new ResponseEntity<>(serviceLocator.getProductMappers().productsSpecResponse(getOneProductSpecFromDb, "product specification successfully fetch"), HttpStatus.OK);
        }
    }

    private void validateRequestMediaId(Integer mediaId) {
        serviceLocator.getValidationUtils().mediaIdValidation(mediaId);
    }

    private void validateProductId(Integer productId) {
        serviceLocator.getValidationUtils().productIdValidation(productId);
    }

    private void validateRequestToken (String token){
        serviceLocator.getValidationUtils().jwtValidateRequest(token);
    }

    private void validateBruteForceProtection(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private void validationUserRole(String role){
        serviceLocator.getValidationUtils().roleValidation(role);
    }

    private ProductSpecificationDbModel findByIdAndOrganisationIdAndIsActiveAndProductId(Integer id, UUID organisationId, Integer productId) {

        return serviceLocator.getProductSpecificationRepo().findByIdAndOrganisationIdAndIsActiveAndProductId(id, organisationId, true,productId)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }
}
