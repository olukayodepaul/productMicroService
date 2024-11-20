package com.dart.product.service.product_policies_warranty;

import org.springframework.stereotype.Service;

@Service
public class GetAllProductPolicyService {

//    private final ServiceLocator serviceLocator;
//
//    public GetAllProductPolicyService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//
//    public ResponseEntity<ProductPolicyAllResModel> getAllProductPolicy(String token, Integer productId) {
//
//        validateRequestToken(token);
//        validateProductId(productId);
//
//        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
//        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
//        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
//        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);
//
//        validationUserRole(roles);
//        validateBruteForceProtection(plainUUID);
//
//        FetchAllProductPolicyModel fetchAllProductPolicyFromCache = serviceLocator.getRedisProductCacheRepo().findAllProductPolicy(organisationId.toString(), productId);
//
//        if(fetchAllProductPolicyFromCache.getStatus()){
//
//            List<ProductPolicyDbModel> allProductPolicy = serviceLocator.getProductMappers().mapAllCacheToProductPolicy(fetchAllProductPolicyFromCache.getProductPolicy());
//            return new ResponseEntity<>(serviceLocator.getProductMappers().productPolicyResponseBuilder(allProductPolicy), HttpStatus.OK);
//
//        }else{
//            List<ProductPolicyDbModel> allProductPolicy = findByOrganisationIdAndIsActiveAndProductId(organisationId, productId);
//            validateIfProductPolicyExists(allProductPolicy);
//            return new ResponseEntity<>(serviceLocator.getProductMappers().productPolicyResponseBuilder(allProductPolicy), HttpStatus.OK);
//        }
//
//    }
//
//    private void validateIfProductPolicyExists(List<ProductPolicyDbModel> productSpec) {
//        if (productSpec.isEmpty()) {
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, HttpStatus.NOT_FOUND.toString(), AppConfig.FETCH_ALL_PRODUCT_MEDIA),
//                    HttpStatus.NOT_FOUND
//            );
//        }
//    }
//
//    private void validateProductId(Integer productId) {
//        serviceLocator.getValidationUtils().productIdValidation(productId);
//    }
//
//    private void validateRequestToken (String token){
//        serviceLocator.getValidationUtils().jwtValidateRequest(token);
//    }
//
//    private void validateBruteForceProtection(String uuid) {
//        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
//    }
//
//    private void validationUserRole(String role){
//        serviceLocator.getValidationUtils().roleValidation(role);
//    }
//
//    private List<ProductPolicyDbModel> findByOrganisationIdAndIsActiveAndProductId(UUID organisationId, Integer productId) {
//
//        return serviceLocator.getProductPolicyRepo().findByOrganisationIdAndIsActiveAndProductId(organisationId, true, productId)
//                .orElseThrow(() -> new CustomRuntimeException(
//                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
//                        HttpStatus.NOT_FOUND
//                ));
//    }

}
