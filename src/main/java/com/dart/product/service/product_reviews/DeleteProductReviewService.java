package com.dart.product.service.product_reviews;


import org.springframework.stereotype.Service;

@Service
public class DeleteProductReviewService {


//    private final ServiceLocator serviceLocator;
//
//    public DeleteProductReviewService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<ProductReviewOneResModel> updateProductPolicy(
//            String token,  Integer productId, Integer id) {
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
//        ProductReviewDbModel isProductReviewExistingInDb =  findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);
//
//        isProductReviewExistingInDb.setUpdatedAt(LocalDateTime.now());
//        isProductReviewExistingInDb.setActive(false);
//        SaveAndUpdateProductReviewResponse updateRecordInDb = serviceLocator
//                .getSaveAndUpdateRecord()
//                .saveProductReview(isProductReviewExistingInDb);
//
//        isRecordDeletedInTheDb(updateRecordInDb);
//
//        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo().deleteProductReview(serviceLocator.getProductMappers().mapProductReviewCacheModelToDbModel(updateRecordInDb.getProductReviews()));
//
//        isRecordSaveInTheCache(cacheRecordInMemory);
//
//        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
//        return new ResponseEntity<>(serviceLocator.getProductMappers().productReviewOneResponseBuilder(updateRecordInDb.getProductReviews(), "product policy successfully deleted"), HttpStatus.OK);
//
//    }
//
//    private void isRecordSaveInTheCache(boolean isRecord) {
//        if(!isRecord){
//            //send through kafka
//        }
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
//    private void isRecordDeletedInTheDb(SaveAndUpdateProductReviewResponse isSave) {
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
