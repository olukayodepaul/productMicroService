package com.dart.product.service.product_specification;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_specification_model.FetchAllProductSpecModel;
import com.dart.product.entity.product_specification_model.FetchAllProductSpecResModel;
import com.dart.product.entity.product_specification_model.ProductSpecificationDbModel;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetAllProductSpecService {

    private final ServiceLocator serviceLocator;

    public GetAllProductSpecService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<FetchAllProductSpecResModel> getAllProductSpec(Integer productId, String token) {

        validateRequestToken(token);
        validateProductId(productId);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        FetchAllProductSpecModel fetchAllProductSpecFromCache = serviceLocator.getRedisProductCacheRepo().findAllProductSpec(organisationId.toString(), productId);

        if(fetchAllProductSpecFromCache.getStatus()) {

            List<ProductSpecificationDbModel> allProductSpec = serviceLocator.getProductMappers().mapAllCacheToProductSpec(fetchAllProductSpecFromCache.getProductSpec());
            return new ResponseEntity<>(serviceLocator.getProductMappers().productsSpecFetchAllResponse(allProductSpec), HttpStatus.OK);

        }else{

            List<ProductSpecificationDbModel> allProductSpec = findByOrganisationIdAndIsActiveAndProductId(organisationId, productId);
            validateIfProductSpecExists(allProductSpec);
            serviceLocator.getProductMappers().productsSpecFetchAllResponse(allProductSpec);
            return new ResponseEntity<>(serviceLocator.getProductMappers().productsSpecFetchAllResponse(allProductSpec), HttpStatus.OK);

        }
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

    private List<ProductSpecificationDbModel> findByOrganisationIdAndIsActiveAndProductId(UUID organisationId, Integer productId) {

        return serviceLocator.getProductSpecificationRepo().findByOrganisationIdAndIsActiveAndProductId(organisationId, true,productId)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    private void validateIfProductSpecExists(List<ProductSpecificationDbModel> productSpec) {
        if (productSpec.isEmpty()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, HttpStatus.NOT_FOUND.toString(), AppConfig.FETCH_ALL_PRODUCT_MEDIA),
                    HttpStatus.NOT_FOUND
            );
        }
    }


}
