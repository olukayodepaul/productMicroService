package com.dart.product.service.product_policies;


import org.springframework.stereotype.Service;

@Service
public class UpdateProductPolicyService {

//    private final ServiceLocator serviceLocator;
//
//    public UpdateProductPolicyService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<ProductPolicyOneResModel> updateProductPolicy(String token, AddProductPolicyReqModel reqBody, Integer productId, Integer id
//    ) {
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
//        ProductPolicyDbModel isProductPolicyExistingInDb =  findByIdAndOrganisationIdAndIsActive(id, organisationId, productId);
//
//
//        reqBody.setOrganisation_id(isProductPolicyExistingInDb.getOrganisationId());
//        reqBody.setCreated_at(isProductPolicyExistingInDb.getCreatedAt());
//        reqBody.setUpdated_at(LocalDateTime.now());
//        reqBody.set_active(isProductPolicyExistingInDb.isActive());
//        reqBody.setId(isProductPolicyExistingInDb.getId());
//        SaveAndUpdateProductPolicyResponse saveRecordInDb = serviceLocator
//                .getSaveAndUpdateRecord()
//                .saveProductPolicy(serviceLocator.getProductMappers().mapAddProductPolicyReqModelToDbModel(reqBody));
//
//        isRecordUpdatedInTheDb(saveRecordInDb);
//
//        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo()
//                .saveUpdateProductPolicy(serviceLocator.getProductMappers().mapProductPolicyToCache(saveRecordInDb.getProductPolicy()));
//
//        isRecordUpdatedInTheCache(cacheRecordInMemory);
//
//        //todo: send newly updated product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
//
//        return new ResponseEntity<>(serviceLocator.getProductMappers().productPolicyOneResponseBuilder(saveRecordInDb.getProductPolicy(), "product policy successfully updated"), HttpStatus.OK);
//    }
//
//    private void isRecordUpdatedInTheCache(boolean isRecord) {
//        if(!isRecord){
//            //send through kafka
//        }
//    }
//
//    private void validateRequestToken(String token) {
//        serviceLocator.getValidationUtils().jwtValidateRequest(token);
//    }
//
//    private void validateRequestBody(AddProductPolicyReqModel reqBody) {
//       // serviceLocator.getValidationUtils().productSpecValidate(reqBody);
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
//    private ProductPolicyDbModel findByIdAndOrganisationIdAndIsActive(Integer id, UUID organisationId, Integer productId) {
//        return serviceLocator.getProductPolicyRepo().findByIdAndOrganisationIdAndIsActiveAndProductId(id, organisationId, true, productId)
//                .orElseThrow(() -> new CustomRuntimeException(
//                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
//                        HttpStatus.NOT_FOUND
//                ));
//    }
//
//    private void isRecordUpdatedInTheDb(SaveAndUpdateProductPolicyResponse isSave) {
//        if(!isSave.getStatus()) {
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//    }
//


}
