package com.dart.product.service.related_products;

import org.springframework.stereotype.Service;

@Service
public class GetOneRelatedProductsService {

//    private final ServiceLocator serviceLocator;
//
//    public GetOneRelatedProductsService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<RelatedProductsOneResModel> addProductPolicy(
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
//        FetchOneRelatedProductsModel fetchOneRelatedProductFromCache  = serviceLocator.getRedisProductCacheRepo().findOneRelatedProducts(organisationId.toString(), productId, id);
//
//        if(fetchOneRelatedProductFromCache.getStatus()) {
//            return new ResponseEntity<>(serviceLocator.getProductMappers().relatedProductsOneResponseBuilder(serviceLocator.getProductMappers().mapDbModelToRelatedProductsCacheModel(fetchOneRelatedProductFromCache.getRelatedProducts()), "related  product successfully fetch"), HttpStatus.OK);
//        } else {
//            RelatedProductsDbModel isRelatedProductExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);
//            return new ResponseEntity<>(serviceLocator.getProductMappers().relatedProductsOneResponseBuilder(isRelatedProductExistingInDb, "related  product successfully fetch"), HttpStatus.OK);
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
//    private RelatedProductsDbModel findByIdAndOrganisationIdAndIsActive(Integer id, Integer productId, UUID organisationId) {
//        return serviceLocator.getRelatedProductsDbModel().findByIdAndProductIdAndOrganisationIdAndIsActive(id,  productId, organisationId, true)
//                .orElseThrow(() -> new CustomRuntimeException(
//                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
//                        HttpStatus.NOT_FOUND
//                ));
//    }
}
