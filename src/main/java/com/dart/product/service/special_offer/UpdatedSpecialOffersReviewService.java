package com.dart.product.service.special_offer;

import org.springframework.stereotype.Service;

@Service
public class UpdatedSpecialOffersReviewService {

//    private final ServiceLocator serviceLocator;
//
//    public UpdatedSpecialOffersReviewService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<SpecialOffersOneResModel> addProductPolicy(
//            String token, AddSpecialOffersReqModel reqBody, Integer productId, Integer id) {
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
//        SpecialOffersDbModel isSpecialOfferExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);
//
//        reqBody.setOrganisation_id(isSpecialOfferExistingInDb.getOrganisationId());
//        reqBody.setCreated_at(isSpecialOfferExistingInDb.getCreatedAt());
//        reqBody.setUpdated_at(LocalDateTime.now());
//        reqBody.set_active(isSpecialOfferExistingInDb.isActive());
//        reqBody.setId(isSpecialOfferExistingInDb.getId());
//        SaveAndUpdateSpecialOffersResponse updateRecordInDb = serviceLocator
//                .getSaveAndUpdateRecord()
//                .saveSpecialOffer(serviceLocator.getProductMappers().mapAddSpecialOfferModelToDbModel(reqBody));
//
//
//        isRecordSaveInTheDb(updateRecordInDb);
//        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo().saveUpdateSpecialOffers(serviceLocator.getProductMappers().mapSpecialOffersDbModelToDbModel(updateRecordInDb.getSpecialOffers()));
//
//        isRecordSaveInTheCache(cacheRecordInMemory);
//
//        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
//        return new ResponseEntity<>(serviceLocator.getProductMappers().specialOffersOneResponseBuilder(updateRecordInDb.getSpecialOffers(), "special offer successfully updated"), HttpStatus.OK);
//
//    }
//
//    private void isRecordSaveInTheCache(boolean isRecord) {
//        if(!isRecord){
//            //send through kafka
//        }
//    }
//
//    private void validateRequestBody(AddSpecialOffersReqModel reqBody) {
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
//    private void isRecordSaveInTheDb(SaveAndUpdateSpecialOffersResponse isSave) {
//        if(!isSave.getStatus()) {
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//    }
//
//    private SpecialOffersDbModel findByIdAndOrganisationIdAndIsActive(Integer id, Integer productId, UUID organisationId) {
//        return serviceLocator.getSpecialOffersRepo().findByIdAndProductIdAndOrganisationIdAndIsActive(id,  productId, organisationId, true)
//                .orElseThrow(() -> new CustomRuntimeException(
//                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
//                        HttpStatus.NOT_FOUND
//                ));
//    }

}
