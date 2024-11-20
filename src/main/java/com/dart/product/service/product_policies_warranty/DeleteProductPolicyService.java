package com.dart.product.service.product_policies_warranty;


import org.springframework.stereotype.Service;


@Service
public class DeleteProductPolicyService {

//    private final ServiceLocator serviceLocator;
//
//    public DeleteProductPolicyService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<ProductPolicyOneResModel> deleteProductPolicy(String token, Integer productId, Integer id) {
//
//        validateRequestToken(token);
//        //validateProductId(productId);
//        //validateId(id);
//
//
//        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
//        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
//        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
//        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);
//
//        validationUserRole(roles);
//        validateBruteForceProtection(plainUUID);
//
//        ProductPolicyDbModel isProductPolicyExistingInDb =  findByIdAndOrganisationIdAndIsActive(id, organisationId, productId);
//
//        isProductPolicyExistingInDb.setActive(false);
//        isProductPolicyExistingInDb.setUpdatedAt(LocalDateTime.now());
//        SaveAndUpdateProductPolicyResponse deleteRecordInDb = serviceLocator
//                .getSaveAndUpdateRecord().saveProductPolicy(isProductPolicyExistingInDb);
//
//        validateIfRecordIsDeleted(deleteRecordInDb);
//
//        boolean deleteCacheRecord = serviceLocator.getRedisProductCacheRepo()
//                .deleteProductPolicy(serviceLocator.getProductMappers().mapProductPolicyToCache(deleteRecordInDb.getProductPolicy()));
//
//        validateIfCacheIsDeleted(deleteCacheRecord);
//
//        return new ResponseEntity<>(serviceLocator.getProductMappers().productPolicyOneResponseBuilder(deleteRecordInDb.getProductPolicy(), "product policy successfully deleted"), HttpStatus.OK);
//    }
//
//    private void validateIfCacheIsDeleted(boolean isRecord) {
//        if(!isRecord){
//            //send through kafka
//        }
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
//    private void validateRequestToken(String token) {
//        serviceLocator.getValidationUtils().jwtValidateRequest(token);
//    }
//
//    private ProductPolicyDbModel findByIdAndOrganisationIdAndIsActive(Integer id, UUID organisationId, Integer ProductId) {
//        return serviceLocator.getProductPolicyRepo().findByIdAndOrganisationIdAndIsActiveAndProductId(id, organisationId, true, ProductId)
//                .orElseThrow(() -> new CustomRuntimeException(
//                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
//                        HttpStatus.NOT_FOUND
//                ));
//    }
//
//    private void validateIfRecordIsDeleted(SaveAndUpdateProductPolicyResponse isSave) {
//        if(!isSave.getStatus()) {
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//    }

}
