package com.dart.product.service.product_tag;

import org.springframework.stereotype.Service;

@Service
public class GetOneProductTagService {
//
//    private final ServiceLocator serviceLocator;
//
//    public GetOneProductTagService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<ProductTagOneResModel> getOneProductPolicy(
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
//        FetchOneProductTagModel fetchOneProductTagFromCache = serviceLocator.getRedisProductCacheRepo().findOneProductTag(organisationId.toString(), productId, id);
//
//        if(fetchOneProductTagFromCache.getStatus()) {
//            return new ResponseEntity<>(serviceLocator.getProductMappers().productTagResponseBuilder(serviceLocator.getProductMappers().mapDbModelToProductTagDbModel(fetchOneProductTagFromCache.getProductTag()), "product tag successfully fetch"), HttpStatus.OK);
//        }else{
//            ProductTagDbModel isProductTagExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);
//            return new ResponseEntity<>(serviceLocator.getProductMappers().productTagResponseBuilder(isProductTagExistingInDb, "product tag successfully fetch"), HttpStatus.OK);
//        }
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
//    private ProductTagDbModel findByIdAndOrganisationIdAndIsActive(Integer id, Integer productId, UUID organisationId) {
//        return serviceLocator.getProductTagRepo().findByIdAndProductIdAndOrganisationIdAndIsActive(id,  productId, organisationId, true)
//                .orElseThrow(() -> new CustomRuntimeException(
//                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
//                        HttpStatus.NOT_FOUND
//                ));
//    }
}
