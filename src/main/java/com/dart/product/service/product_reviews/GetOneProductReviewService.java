package com.dart.product.service.product_reviews;


import org.springframework.stereotype.Service;

@Service
public class GetOneProductReviewService {

//
//    private final ServiceLocator serviceLocator;
//
//    public GetOneProductReviewService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<ProductReviewOneResModel> getOneProductPolicy(
//            String token,  Integer productId, Integer id) {
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
//        FetchOneProductReviewModel fetchOneProductReviewFromCache = serviceLocator.getRedisProductCacheRepo().findOneProductReview(organisationId.toString(), productId, id);
//
//        if(fetchOneProductReviewFromCache.getStatus()) {
//            return new ResponseEntity<>(serviceLocator.getProductMappers().productReviewOneResponseBuilder(serviceLocator.getProductMappers().mapDbModelToProductReviewCacheModel(fetchOneProductReviewFromCache.getProductReview()), "product policy successfully fetch"), HttpStatus.OK);
//        }else{
//            ProductReviewDbModel isProductReviewExistingInDb =  findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);
//            return new ResponseEntity<>(serviceLocator.getProductMappers().productReviewOneResponseBuilder(isProductReviewExistingInDb, "product policy successfully fetch"), HttpStatus.OK);
//        }
//
//    }
//
//
//    private void validateRequestToken(String token) {
//        serviceLocator.getValidationUtils().jwtValidateRequest(token);
//    }
//
//    private void validationUserRole(String role){
//        serviceLocator.getValidationUtils().customerRoleValidation(role);
//    }
//
//    private void validateBruteForceProtection(String uuid) {
//        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
//    }
//
//    private ProductReviewDbModel findByIdAndOrganisationIdAndIsActive(Integer id, Integer productId, UUID organisationId) {
//        return serviceLocator.getProductReviewRepo().findByIdAndProductIdAndOrganisationIdAndIsActive(id,  productId, organisationId, true)
//                .orElseThrow(() -> new CustomRuntimeException(
//                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
//                        HttpStatus.NOT_FOUND
//                ));
//    }

}
