package com.dart.product.service.special_offer;

import org.springframework.stereotype.Service;

@Service
public class GetOneSpecialOffersReviewService {
//
//    private final ServiceLocator serviceLocator;
//
//    public GetOneSpecialOffersReviewService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<SpecialOffersOneResModel> addProductPolicy(
//            String token, Integer productId, Integer id) {
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
//        FetchOneSpecialOfferModel fetchOneSpecialOfferFromCache = serviceLocator.getRedisProductCacheRepo().findOneSpecialOffer(organisationId.toString(), productId, id);
//
//        if(fetchOneSpecialOfferFromCache.getStatus()){
//            return new ResponseEntity<>(serviceLocator.getProductMappers().specialOffersOneResponseBuilder(serviceLocator.getProductMappers().mapDbModelToSpecialOffersDbModel(fetchOneSpecialOfferFromCache.getSpecialOffer()), "special offer successfully fetch"), HttpStatus.OK);
//        }else{
//            SpecialOffersDbModel isSpecialOfferExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);
//            return new ResponseEntity<>(serviceLocator.getProductMappers().specialOffersOneResponseBuilder(isSpecialOfferExistingInDb, "special offer successfully fetch"), HttpStatus.OK);
//        }
//
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
//    private void isRecordDeletedInTheDb(SaveAndUpdateSpecialOffersResponse isSave) {
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
