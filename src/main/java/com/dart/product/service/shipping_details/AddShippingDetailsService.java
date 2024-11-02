package com.dart.product.service.shipping_details;


import org.springframework.stereotype.Service;

@Service
public class AddShippingDetailsService {

//    private final ServiceLocator serviceLocator;
//
//    public AddShippingDetailsService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<ShippingDetailsOneResModel> addShippingDetails(String token, AddShippingDetailsReqModel reqBody) {
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
//        SaveAndUpdateShippingDetailsResponse saveRecordInDb = serviceLocator.getSaveAndUpdateRecord().saveShippingDetails(
//                serviceLocator.getProductMappers().mapAddShippingDetailsReqModelToDbModel(reqBody)
//        );
//
//        isRecordSaveInTheDb(saveRecordInDb);
//
//        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo()
//                .saveUpdateShippingDetails(serviceLocator.getProductMappers().mapShippingDetailsCacheModelToDbModel(saveRecordInDb.getShippingDetails()));
//
//        isRecordSaveInTheCache(cacheRecordInMemory);
//
//        return new ResponseEntity<>(serviceLocator.getProductMappers().shippingDetailsOneResponseBuilder
//                (saveRecordInDb.getShippingDetails(),"Shipping details successfully created"), HttpStatus.CREATED);
//    }
//
//    private void validateRequestToken(String token) {
//        serviceLocator.getValidationUtils().jwtValidateRequest(token);
//    }
//
//    private void validateRequestBody(AddShippingDetailsReqModel reqBody) {
//        serviceLocator.getValidationUtils().shippingDetailsValidation(reqBody);
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
//    private void isRecordSaveInTheDb(SaveAndUpdateShippingDetailsResponse isSave) {
//        if(!isSave.getStatus()) {
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//    }
//
//    private void isRecordSaveInTheCache(boolean isRecord) {
//        if(!isRecord){
//            //send through kafka
//        }
//    }

}
