package com.dart.product.service.product_specification;

import org.springframework.stereotype.Service;

@Service
public class DeleteProductSpecService {

//
//    private final ServiceLocator serviceLocator;
//
//    public DeleteProductSpecService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//
//    public ResponseEntity<AddProductSpecResModel> updateProductSpec(Integer id, String token, Integer productId) {
//
//        validateRequestToken(token);
//        validationUserId(id);
//       // validateProductId(productId);
//
//        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
//        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
//        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
//        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);
//
//        validationUserRole(roles);
//        validateBruteForceProtection(plainUUID);
//
//        ProductSpecificationDbModel isProductSpecExistingInDb =  findByIdAndOrganisationIdAndIsActive(organisationId, productId);
//
//        isProductSpecExistingInDb.setActive(false);
//        isProductSpecExistingInDb.setUpdatedAt(LocalDateTime.now());
//        SaveAndUpdateProductSpecResponse deleteRecordInDb = serviceLocator
//                .getSaveAndUpdateRecord()
//                .saveProductSpecification(serviceLocator.getProductMappers().mapProductSpecToProductSpec(isProductSpecExistingInDb));
//
//        validateIfRecordIsDeleted(deleteRecordInDb);
//
//        boolean deleteCacheRecord = serviceLocator.getRedisProductCacheRepo()
//                .deleteProductSpec(serviceLocator.getProductMappers().mapProductSpecToCache(deleteRecordInDb.getProductSpec()));
//
//        validateIfCacheIsDeleted(deleteCacheRecord);
//
//        return new ResponseEntity<>(serviceLocator.getProductMappers().productsSpecResponse(deleteRecordInDb.getProductSpec(), "product specification successfully deleted"), HttpStatus.OK);
//
//    }
//
//    private void validateIfCacheIsDeleted(boolean isRecord) {
//        if(!isRecord){
//            //send through kafka
//        }
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
//    private void validationUserId(Integer id){
//        serviceLocator.getValidationUtils().productSpecValidation(id);
//    }
//
//    private ProductSpecificationDbModel findByIdAndOrganisationIdAndIsActive(UUID organisationId, Integer ProductId) {
//        return serviceLocator.getProductSpecificationRepo().findByOrganisationIdAndIsActiveAndProductId(organisationId, true, ProductId)
//                .orElseThrow(() -> new CustomRuntimeException(
//                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
//                        HttpStatus.NOT_FOUND
//                ));
//    }
//
//    private void validateIfRecordIsDeleted(SaveAndUpdateProductSpecResponse isSave) {
//        if(!isSave.getStatus()) {
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//    }

}
