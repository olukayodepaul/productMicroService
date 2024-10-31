package com.dart.product.service.special_offer;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.special_offers_model.AddSpecialOffersReqModel;
import com.dart.product.entity.special_offers_model.SaveAndUpdateSpecialOffersResponse;
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
public class AddSpecialOffersReviewService {

    private final ServiceLocator serviceLocator;

    public AddSpecialOffersReviewService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<SpecialOffersOneResModel> addProductPolicy(
            String token, AddSpecialOffersReqModel reqBody) {

        validateRequestToken(token);
        validateRequestBody(reqBody);


        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);

        validationUserRole(roles);
        validateBruteForceProtection(plainUUID);

        reqBody.setOrganisation_id(organisationId);
        reqBody.setCreated_at(LocalDateTime.now());
        reqBody.setUpdated_at(LocalDateTime.now());
        reqBody.set_active(true);
        reqBody.setId(0);

        SaveAndUpdateSpecialOffersResponse saveRecordInDb = serviceLocator
                .getSaveAndUpdateRecord()
                .saveSpecialOffer(serviceLocator.getProductMappers().mapAddSpecialOfferModelToDbModel(reqBody));


        isRecordSaveInTheDb(saveRecordInDb);
        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo().saveUpdateSpecialOffers(serviceLocator.getProductMappers().mapSpecialOffersDbModelToDbModel(saveRecordInDb.getSpecialOffers()));

        isRecordSaveInTheCache(cacheRecordInMemory);

        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
        return new ResponseEntity<>(serviceLocator.getProductMappers().specialOffersOneResponseBuilder(saveRecordInDb.getSpecialOffers(), "special offer successfully created"), HttpStatus.CREATED);

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

}
