package com.dart.product.service.product_specification;

import org.springframework.stereotype.Service;

@Service
public class UpdateProductSpecService {

//    private final ServiceLocator serviceLocator;
//
//    public UpdateProductSpecService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<AddProductSpecResModel> updateProductSpec(AddProductSpecReqModel reqBody, String token, Integer productId, Integer id) {
//
//        validateRequestToken(token);
//        validateRequestBody(reqBody);
//        //validateProductId(productId);
//
//        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
//        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
//        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
//        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);
//
//        validationUserRole(roles);
//        validateBruteForceProtection(plainUUID);
//
//
//        ProductSpecificationDbModel isProductSpecExistingInDb =  findByIdAndOrganisationIdAndIsActive(organisationId, productId);
//
//        reqBody.setOrganisation_id(isProductSpecExistingInDb.getOrganisationId());
//        reqBody.setCreated_at(isProductSpecExistingInDb.getCreatedAt());
//        reqBody.setUpdated_at(LocalDateTime.now());
//        reqBody.set_active(isProductSpecExistingInDb.isActive());
//        reqBody.setId(isProductSpecExistingInDb.getId());
//        SaveAndUpdateProductSpecResponse updateRecordInDb = serviceLocator
//                .getSaveAndUpdateRecord()
//                .saveProductSpecification(serviceLocator.getProductMappers().mapProductSpec(reqBody));
//
//        validateIfRecordIsUpdated(updateRecordInDb);
//
//        boolean cacheRecord = serviceLocator.getRedisProductCacheRepo()
//                .saveUpdateProductSpec(serviceLocator.getProductMappers().mapProductSpecToCache(updateRecordInDb.getProductSpec()));
//
//        validateIfRecordIsCache(cacheRecord);
//
//        //todo: send newly updated product specification to searchMicroService through (grpc) if fail then, kafka using same proto buffer
//
//        return new ResponseEntity<>(serviceLocator.getProductMappers().productsSpecResponse(updateRecordInDb.getProductSpec(), "product specification successfully updated"), HttpStatus.OK);
//
//    }
//
//    private void validateIfRecordIsCache(boolean isRecord) {
//        if(!isRecord){
//            //send through kafka
//        }
//    }
//
//    private void validateIfRecordIsUpdated(SaveAndUpdateProductSpecResponse isSave) {
//        if(!isSave.getStatus()) {
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//    }
//
//    private void validateRequestToken(String token) {
//        serviceLocator.getValidationUtils().jwtValidateRequest(token);
//    }
//
//    private void validateRequestBody(AddProductSpecReqModel reqBody) {
//        serviceLocator.getValidationUtils().productSpecValidate(reqBody);
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
//    private ProductSpecificationDbModel findByIdAndOrganisationIdAndIsActive(UUID organisationId, Integer productId) {
//        return serviceLocator.getProductSpecificationRepo().findByOrganisationIdAndIsActiveAndProductId(organisationId, true, productId)
//                .orElseThrow(() -> new CustomRuntimeException(
//                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
//                        HttpStatus.NOT_FOUND
//                ));
//    }

}
