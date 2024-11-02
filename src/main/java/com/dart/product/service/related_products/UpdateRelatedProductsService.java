package com.dart.product.service.related_products;

import org.springframework.stereotype.Service;

@Service
public class UpdateRelatedProductsService {

//    private final ServiceLocator serviceLocator;
//
//    public UpdateRelatedProductsService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<RelatedProductsOneResModel> addProductPolicy(
//            String token, AddRelatedProductsReqModel reqBody,Integer productId, Integer id) {
//
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
//        RelatedProductsDbModel isRelatedProductExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);
//
//
//        reqBody.setOrganisation_id(isRelatedProductExistingInDb.getOrganisationId());
//        reqBody.setCreated_at(isRelatedProductExistingInDb.getCreatedAt());
//        reqBody.setUpdated_at(LocalDateTime.now());
//        reqBody.set_active(isRelatedProductExistingInDb.isActive());
//        reqBody.setId(isRelatedProductExistingInDb.getId());
//        SaveAndUpdateRelatedProductResponse updatedRecordInDb = serviceLocator
//                .getSaveAndUpdateRecord()
//                .saveRelatedProduct(serviceLocator.getProductMappers().mapAddRelatedProductModelToDbModel(reqBody));
//
//
//        isRecordSaveInTheDb(updatedRecordInDb);
//        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo().saveUpdateRelatedProduct(serviceLocator.getProductMappers().mapRelatedProductsCacheModelToDbModel(updatedRecordInDb.getRelatedProducts()));
//
//        isRecordSaveInTheCache(cacheRecordInMemory);
//
//        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
//        return new ResponseEntity<>(serviceLocator.getProductMappers().relatedProductsOneResponseBuilder(updatedRecordInDb.getRelatedProducts(), "related  product successfully created"), HttpStatus.OK);
//
//    }
//
//    private void isRecordSaveInTheCache(boolean isRecord) {
//        if(!isRecord){
//            //send through kafka
//        }
//    }
//
//    private void validateRequestBody(AddRelatedProductsReqModel reqBody) {
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
//    private void isRecordSaveInTheDb(SaveAndUpdateRelatedProductResponse isSave) {
//        if(!isSave.getStatus()) {
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
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
