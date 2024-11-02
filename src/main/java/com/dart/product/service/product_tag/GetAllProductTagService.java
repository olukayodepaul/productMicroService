package com.dart.product.service.product_tag;

import org.springframework.stereotype.Service;

@Service
public class GetAllProductTagService {

//    private final ServiceLocator serviceLocator;
//
//    public GetAllProductTagService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<ProductTagAllResModel> getAllProductPolicy(
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
//        FetchAllProductTagModel fetchAllProductTagFromCache = serviceLocator.getRedisProductCacheRepo().findAllProductTag(organisationId.toString(), productId);
//
//        if(fetchAllProductTagFromCache.getStatus()) {
//            List<ProductTagDbModel> allProductTag = serviceLocator.getProductMappers().mapToAllProductTag(fetchAllProductTagFromCache.getProductTag());
//            return new ResponseEntity<>(serviceLocator.getProductMappers().allProductTagResponseBuilder(allProductTag, "product tag successfully fetch"), HttpStatus.OK);
//        }
//
//        List<ProductTagDbModel> allProductTag = findByIdAndOrganisationIdAndIsActive(productId, organisationId);
//        return new ResponseEntity<>(serviceLocator.getProductMappers().allProductTagResponseBuilder(allProductTag, "product tag successfully fetch"), HttpStatus.OK);
//
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
//    private List<ProductTagDbModel> findByIdAndOrganisationIdAndIsActive(Integer productId, UUID organisationId) {
//        return serviceLocator.getProductTagRepo().findByProductIdAndOrganisationIdAndIsActive(productId, organisationId, true)
//                .orElseThrow(() -> new CustomRuntimeException(
//                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
//                        HttpStatus.NOT_FOUND
//                ));
//    }
}
