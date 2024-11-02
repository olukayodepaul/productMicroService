package com.dart.product.service.shipping_details;


import org.springframework.stereotype.Service;

@Service
public class UpdateShippingDetailsService {

//    private final ServiceLocator serviceLocator;
//
//    public UpdateShippingDetailsService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<ShippingDetailsOneResModel> updateShippingDetails(
//            String token, AddShippingDetailsReqModel reqBody, Integer  productId, Integer id)
//    {
//
//        validateRequestToken(token);
//        validateRequestBody(reqBody); //product_id is optional here
//
//        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
//        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
//        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
//        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);
//
//        validationUserRole(roles);
//        validateBruteForceProtection(plainUUID);
//
//        ShippingDetailsDbModel isShippingDetailsExistingInDb = findByIdAndOrganisationIdAndIsActive( id, organisationId,  productId);
//
//        reqBody.setOrganisation_id(isShippingDetailsExistingInDb.getOrganisationId());
//        reqBody.setCreated_at(isShippingDetailsExistingInDb.getCreatedAt());
//        reqBody.setUpdated_at(isShippingDetailsExistingInDb.getUpdatedAt());
//        reqBody.set_active(isShippingDetailsExistingInDb.isActive());
//        reqBody.setId(isShippingDetailsExistingInDb.getId());
//        SaveAndUpdateShippingDetailsResponse updateRecordInDb = serviceLocator.getSaveAndUpdateRecord().saveShippingDetails(
//                serviceLocator.getProductMappers().mapAddShippingDetailsReqModelToDbModel(reqBody)
//        );
//
//        isRecordUpdatedInTheDb(updateRecordInDb);
//
//        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo()
//                .saveUpdateShippingDetails(serviceLocator.getProductMappers().mapShippingDetailsCacheModelToDbModel(updateRecordInDb.getShippingDetails()));
//
//        validateIfRecordIsCache(cacheRecordInMemory);
//
//        return new ResponseEntity<>(serviceLocator.getProductMappers().shippingDetailsOneResponseBuilder
//                (updateRecordInDb.getShippingDetails(),"Shipping details successfully updated"), HttpStatus.OK);
//
//    }
//
//    private void validateIfRecordIsCache(boolean isRecord) {
//        if(!isRecord){
//            //send through kafka
//        }
//    }
//
//    private void isRecordUpdatedInTheDb(SaveAndUpdateShippingDetailsResponse isSave) {
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
//    private ShippingDetailsDbModel findByIdAndOrganisationIdAndIsActive(Integer id, UUID organisationId, Integer productId) {
//        return serviceLocator.getShippingDetailsRepo().findByIdAndOrganisationIdAndProductIdAndIsActive(id, organisationId, productId, true)
//                .orElseThrow(() -> new CustomRuntimeException(
//                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
//                        HttpStatus.NOT_FOUND
//                ));
//    }

}
