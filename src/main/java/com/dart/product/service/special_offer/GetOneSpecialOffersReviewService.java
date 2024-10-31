package com.dart.product.service.special_offer;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.related_products_model.FetchOneRelatedProductsModel;
import com.dart.product.entity.special_offers_model.FetchOneSpecialOfferModel;
import com.dart.product.entity.special_offers_model.SaveAndUpdateSpecialOffersResponse;
import com.dart.product.entity.special_offers_model.SpecialOffersDbModel;
import com.dart.product.entity.special_offers_model.SpecialOffersOneResModel;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class GetOneSpecialOffersReviewService {

    private final ServiceLocator serviceLocator;

    public GetOneSpecialOffersReviewService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<SpecialOffersOneResModel> addProductPolicy(
            String token, Integer productId, Integer id) {

        validateRequestToken(token);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        FetchOneSpecialOfferModel fetchOneSpecialOfferFromCache = serviceLocator.getRedisProductCacheRepo().findOneSpecialOffer(organisationId.toString(), productId, id);

        if(fetchOneSpecialOfferFromCache.getStatus()){
            return new ResponseEntity<>(serviceLocator.getProductMappers().specialOffersOneResponseBuilder(serviceLocator.getProductMappers().mapDbModelToSpecialOffersDbModel(fetchOneSpecialOfferFromCache.getSpecialOffer()), "special offer successfully fetch"), HttpStatus.OK);
        }else{
            SpecialOffersDbModel isSpecialOfferExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);
            return new ResponseEntity<>(serviceLocator.getProductMappers().specialOffersOneResponseBuilder(isSpecialOfferExistingInDb, "special offer successfully fetch"), HttpStatus.OK);
        }

    }

    private void validateRequestToken(String token) {
        serviceLocator.getValidationUtils().jwtValidateRequest(token);
    }

    private void validationUserRole(String role){
        serviceLocator.getValidationUtils().roleValidation(role);
    }

    private void validateBruteForceProtection(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private void isRecordDeletedInTheDb(SaveAndUpdateSpecialOffersResponse isSave) {
        if(!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private SpecialOffersDbModel findByIdAndOrganisationIdAndIsActive(Integer id, Integer productId, UUID organisationId) {
        return serviceLocator.getSpecialOffersRepo().findByIdAndProductIdAndOrganisationIdAndIsActive(id,  productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}
