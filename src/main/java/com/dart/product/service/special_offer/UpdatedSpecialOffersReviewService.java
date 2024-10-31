package com.dart.product.service.special_offer;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_reviews_model.ProductReviewDbModel;
import com.dart.product.entity.special_offers_model.AddSpecialOffersReqModel;
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
public class UpdatedSpecialOffersReviewService {

    private final ServiceLocator serviceLocator;

    public UpdatedSpecialOffersReviewService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<SpecialOffersOneResModel> addProductPolicy(
            String token, AddSpecialOffersReqModel reqBody, Integer productId, Integer id) {

        validateRequestToken(token);
        validateRequestBody(reqBody);

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        SpecialOffersDbModel isSpecialOfferExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);

        reqBody.setOrganisation_id(isSpecialOfferExistingInDb.getOrganisationId());
        reqBody.setCreated_at(isSpecialOfferExistingInDb.getCreatedAt());
        reqBody.setUpdated_at(LocalDateTime.now());
        reqBody.set_active(isSpecialOfferExistingInDb.isActive());
        reqBody.setId(isSpecialOfferExistingInDb.getId());
        SaveAndUpdateSpecialOffersResponse updateRecordInDb = serviceLocator
                .getSaveAndUpdateRecord()
                .saveSpecialOffer(serviceLocator.getProductMappers().mapAddSpecialOfferModelToDbModel(reqBody));


        isRecordSaveInTheDb(updateRecordInDb);
        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo().saveUpdateSpecialOffers(serviceLocator.getProductMappers().mapSpecialOffersDbModelToDbModel(updateRecordInDb.getSpecialOffers()));

        isRecordSaveInTheCache(cacheRecordInMemory);

        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
        return new ResponseEntity<>(serviceLocator.getProductMappers().specialOffersOneResponseBuilder(updateRecordInDb.getSpecialOffers(), "special offer successfully updated"), HttpStatus.OK);

    }

    private void isRecordSaveInTheCache(boolean isRecord) {
        if(!isRecord){
            //send through kafka
        }
    }

    private void validateRequestBody(AddSpecialOffersReqModel reqBody) {
        //do the validation
        //serviceLocator.getValidationUtils().productSpecValidate(reqBody);
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

    private void isRecordSaveInTheDb(SaveAndUpdateSpecialOffersResponse isSave) {
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
