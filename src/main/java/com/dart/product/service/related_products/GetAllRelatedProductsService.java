package com.dart.product.service.related_products;

import org.springframework.stereotype.Service;

@Service
public class GetAllRelatedProductsService {
//
//    private final ServiceLocator serviceLocator;
//
//    public GetAllRelatedProductsService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<RelatedProductsAllResModel> addProductPolicy(
//            String token, Integer productId, Integer id) {
//
//        validateRequestToken(token);
//
//        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
//        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
//        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
//        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);
//
//        validationUserRole(roles);
//        validateBruteForceProtection(plainUUID);
//
//        FetchAllRelatedProductModel fetchAllRelatedProductFromCache  = serviceLocator.getRedisProductCacheRepo().findAllRelatedProduct(organisationId.toString(), productId);
//
//        if(fetchAllRelatedProductFromCache.getStatus()) {
//            List<RelatedProductsDbModel> allProductReview = serviceLocator.getProductMappers().mapToAllRelatedProduct(fetchAllRelatedProductFromCache.getRelatedProduct());
//            return new ResponseEntity<>(serviceLocator.getProductMappers().allRelatedProductResponseBuilder(allProductReview, "related  product successfully fetch"), HttpStatus.OK);
//        } else {
//            List<RelatedProductsDbModel> isRelatedProductExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);
//            return new ResponseEntity<>(serviceLocator.getProductMappers().allRelatedProductResponseBuilder(isRelatedProductExistingInDb, "related  product successfully fetch"), HttpStatus.OK);
//        }
//    }
//
//    private void validateRequestToken(String token) {
//        serviceLocator.getValidationUtils().jwtValidateRequest(token);
//    }
//
//    private void validationUserRole(String role){
//        serviceLocator.getValidationUtils().roleValidation(role);
//    }
//
//    private void validateBruteForceProtection(String uuid) {
//        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
//    }
//
//    private List<RelatedProductsDbModel> findByIdAndOrganisationIdAndIsActive(Integer id, Integer productId, UUID organisationId) {
//        return serviceLocator.getRelatedProductsDbModel().findByProductIdAndOrganisationIdAndIsActive(productId, organisationId, true)
//                .orElseThrow(() -> new CustomRuntimeException(
//                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
//                        HttpStatus.NOT_FOUND
//                ));
//    }
}
