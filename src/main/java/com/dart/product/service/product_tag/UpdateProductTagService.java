package com.dart.product.service.product_tag;

import org.springframework.stereotype.Service;

@Service
public class UpdateProductTagService {

//    private final ServiceLocator serviceLocator;
//
//    public UpdateProductTagService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<ProductTagOneResModel> updateProductPolicy(
//            String token, AddProductTagReqModel reqBody, Integer productId, Integer id) {
//
//        validateRequestToken(token);
//        validateRequestBody(reqBody);
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
//        ProductTagDbModel isProductTagExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);
//
//        reqBody.setOrganisation_id(isProductTagExistingInDb.getOrganisationId());
//        reqBody.setCreated_at(isProductTagExistingInDb.getCreatedAt());
//        reqBody.setUpdated_at(LocalDateTime.now());
//        reqBody.set_active(isProductTagExistingInDb.isActive());
//        reqBody.setId(isProductTagExistingInDb.getProductId());
//        SaveAndUpdateProductTagResponse updateRecordInDb = serviceLocator
//                .getSaveAndUpdateRecord()
//                .saveProductTag(serviceLocator.getProductMappers().mapAddProductTagModelToDbModel(reqBody));
//
//
//        isRecordSaveInTheDb(updateRecordInDb);
//        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo().saveUpdateProductTag(serviceLocator.getProductMappers().mapProductTagDbModelToDbModel(updateRecordInDb.getProductTags()));
//
//        isRecordSaveInTheCache(cacheRecordInMemory);
//
//        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
//        return new ResponseEntity<>(serviceLocator.getProductMappers().productTagResponseBuilder(updateRecordInDb.getProductTags(), "product tag successfully updated"), HttpStatus.OK);
//
//    }
//
//    private void isRecordSaveInTheCache(boolean isRecord) {
//        if(!isRecord){
//            //send through kafka
//        }
//    }
//
//    private void validateRequestBody(AddProductTagReqModel reqBody) {
//        //do the validation
//        //serviceLocator.getValidationUtils().productSpecValidate(reqBody);
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
//    private void isRecordSaveInTheDb(SaveAndUpdateProductTagResponse isSave) {
//        if(!isSave.getStatus()) {
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
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
