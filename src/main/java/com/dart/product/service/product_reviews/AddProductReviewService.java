package com.dart.product.service.product_reviews;

import org.springframework.stereotype.Service;

@Service
public class AddProductReviewService {

//    private final ServiceLocator serviceLocator;
//
//    public AddProductReviewService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<ProductReviewOneResModel> addProductPolicy(
//            String token, AddProductReviewReqModel reqBody) {
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
//        reqBody.setOrganisation_id(organisationId);
//        reqBody.setCreated_at(LocalDateTime.now());
//        reqBody.setUpdated_at(LocalDateTime.now());
//        reqBody.set_active(true);
//        reqBody.setId(0);
//        SaveAndUpdateProductReviewResponse saveRecordInDb = serviceLocator
//                .getSaveAndUpdateRecord()
//                .saveProductReview(serviceLocator.getProductMappers().mapAddProductReviewReqModelToDbModel(reqBody));
//
//
//        isRecordSaveInTheDb(saveRecordInDb);
//        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo().saveUpdateProductReview(serviceLocator.getProductMappers().mapProductReviewCacheModelToDbModel(saveRecordInDb.getProductReviews()));
//
//        isRecordSaveInTheCache(cacheRecordInMemory);
//
//        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
//        return new ResponseEntity<>(serviceLocator.getProductMappers().productReviewOneResponseBuilder(saveRecordInDb.getProductReviews(), "product policy successfully created"), HttpStatus.CREATED);
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
//    private void isRecordSaveInTheDb(SaveAndUpdateProductReviewResponse isSave) {
//        if(!isSave.getStatus()) {
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//    }


}
