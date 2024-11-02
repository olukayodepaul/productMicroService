package com.dart.product.service.product_tag;

import org.springframework.stereotype.Service;

@Service
public class CreateProductTagService {

//    private final ServiceLocator serviceLocator;
//
//    public CreateProductTagService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<ProductTagOneResModel> addProductPolicy(
//            String token, AddProductTagReqModel reqBody) {
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
//        reqBody.setOrganisation_id(organisationId);
//        reqBody.setCreated_at(LocalDateTime.now());
//        reqBody.setUpdated_at(LocalDateTime.now());
//        reqBody.set_active(true);
//        reqBody.setId(0);
//        SaveAndUpdateProductTagResponse saveRecordInDb = serviceLocator
//                .getSaveAndUpdateRecord()
//                .saveProductTag(serviceLocator.getProductMappers().mapAddProductTagModelToDbModel(reqBody));
//
//
//        isRecordSaveInTheDb(saveRecordInDb);
//        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo().saveUpdateProductTag(serviceLocator.getProductMappers().mapProductTagDbModelToDbModel(saveRecordInDb.getProductTags()));
//
//        isRecordSaveInTheCache(cacheRecordInMemory);
//
//        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
//        return new ResponseEntity<>(serviceLocator.getProductMappers().productTagResponseBuilder(saveRecordInDb.getProductTags(), "product tag successfully created"), HttpStatus.CREATED);
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
//
//    private void validateRequestToken(String token) {
//        serviceLocator.getValidationUtils().accessTokenValidation(token);
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


}
