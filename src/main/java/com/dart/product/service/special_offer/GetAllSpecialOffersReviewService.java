package com.dart.product.service.special_offer;

import org.springframework.stereotype.Service;

@Service
public class GetAllSpecialOffersReviewService {

//    private final ServiceLocator serviceLocator;
//
//    public GetAllSpecialOffersReviewService(ServiceLocator serviceLocator) {
//        this.serviceLocator = serviceLocator;
//    }
//
//    public ResponseEntity<SpecialOffersAllResModel> addProductPolicy(
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
//        FetchAllSpecialOfferModel fetchOneSpecialOfferFromCache = serviceLocator.getRedisProductCacheRepo().findAllSpecialOffer(organisationId.toString(), productId);
//
//        if(fetchOneSpecialOfferFromCache.getStatus()){
//            List<SpecialOffersDbModel> allSpecialOffer = serviceLocator.getProductMappers().mapToAllSpecialOffer(fetchOneSpecialOfferFromCache.getSpecialOffer());
//            return new ResponseEntity<>(serviceLocator.getProductMappers().allSpecialOfferResponseBuilder(allSpecialOffer, "special offer successfully fetch"), HttpStatus.OK);
//        }
//
//        List<SpecialOffersDbModel> allSpecialOffer = findByIdAndOrganisationIdAndIsActive(productId, organisationId);
//        return new ResponseEntity<>(serviceLocator.getProductMappers().allSpecialOfferResponseBuilder(allSpecialOffer, "special offer successfully fetch"), HttpStatus.OK);
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
//    private List<SpecialOffersDbModel> findByIdAndOrganisationIdAndIsActive(Integer productId, UUID organisationId) {
//        return serviceLocator.getSpecialOffersRepo().findByProductIdAndOrganisationIdAndIsActive(productId, organisationId, true)
//                .orElseThrow(() -> new CustomRuntimeException(
//                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
//                        HttpStatus.NOT_FOUND
//                ));
//    }

}
