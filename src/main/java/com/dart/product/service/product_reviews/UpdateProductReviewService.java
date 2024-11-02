package com.dart.product.service.product_reviews;


import org.springframework.stereotype.Service;

@Service
public class UpdateProductReviewService {


//    private final ServiceLocator serviceLocator;
//
//    public UpdateProductReviewService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<ProductReviewOneResModel> updateProductPolicy(
//            String token, AddProductReviewReqModel reqBody, Integer productId, Integer id) {
//
//
//        validateRequestToken(token);
//        validateRequestBody(reqBody);
//
//        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
//        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
//        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
//        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);
//
//        validationUserRole(roles);
//        validateBruteForceProtection(plainUUID);
//
//        ProductReviewDbModel isProductReviewExistingInDb =  findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);
//
//        reqBody.setOrganisation_id(isProductReviewExistingInDb.getOrganisationId());
//        reqBody.setCreated_at(isProductReviewExistingInDb.getCreatedAt());
//        reqBody.setUpdated_at(LocalDateTime.now());
//        reqBody.set_active(isProductReviewExistingInDb.isActive());
//        reqBody.setId(isProductReviewExistingInDb.getId());
//        SaveAndUpdateProductReviewResponse updateRecordInDb = serviceLocator
//                .getSaveAndUpdateRecord()
//                .saveProductReview(serviceLocator.getProductMappers().mapAddProductReviewReqModelToDbModel(reqBody));
//
//        isRecordUpdatedInTheDb(updateRecordInDb);
//
//        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo().saveUpdateProductReview(serviceLocator.getProductMappers().mapProductReviewCacheModelToDbModel(updateRecordInDb.getProductReviews()));
//
//        isRecordSaveInTheCache(cacheRecordInMemory);
//
//        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
//        return new ResponseEntity<>(serviceLocator.getProductMappers().productReviewOneResponseBuilder(updateRecordInDb.getProductReviews(), "product policy successfully updated"), HttpStatus.OK);
//
//    }
//
//    private void isRecordSaveInTheCache(boolean isRecord) {
//        if(!isRecord){
//            //send through kafka
//        }
//    }
//
//    private void validateRequestBody(AddProductReviewReqModel reqBody) {
//        //do the validation
//        //serviceLocator.getValidationUtils().productSpecValidate(reqBody);
//    }
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
//    private void isRecordUpdatedInTheDb(SaveAndUpdateProductReviewResponse isSave) {
//        if(!isSave.getStatus()) {
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
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
