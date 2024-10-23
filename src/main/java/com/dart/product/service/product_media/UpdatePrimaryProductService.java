package com.dart.product.service.product_media;


import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_media_model.MediaDbModel;
import com.dart.product.entity.product_media_model.PrimaryProductResModel;
import com.dart.product.entity.product_media_model.SaveAndUpdateMediaResponse;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdatePrimaryProductService {

    private final ServiceLocator serviceLocator;

    public UpdatePrimaryProductService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Transactional
    public ResponseEntity<PrimaryProductResModel> updateProductPrimaryListing(String token, Integer id) {

        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(serviceLocator.getJwtService().extractUUID(jwtToken));

        validateRequestToken(token);
        validateRequestPrimaryListenBruceForce(plainUUID);
        //check role

        MediaDbModel isProductMediaExisting = findByIdAndOrganisationId(id, organisationId); //false

        //cant update if already you pick a product that is primary
        if (isProductMediaExisting.getIsPrimary()) {
            return null;
        }

        MediaDbModel getPrimaryListing = serviceLocator
                .getProductMediaRepo()
                .findByProductIdAndIsPrimaryAndOrganisationIdAndMediaType(
                        isProductMediaExisting.getProductId(),
                        true,
                        organisationId,
                        isProductMediaExisting.getMediaType()
                ).orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, "fi", AppConfig.UPDATE_PRODUCT_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));

        SaveAndUpdateMediaResponse savePrimaryProductOnFalse = serviceLocator.getSaveAndUpdateRecord().saveProductMedia(serviceLocator.getProductMappers().primaryProductBuilder(getPrimaryListing, false));
        SaveAndUpdateMediaResponse savePrimaryProductOnTrue = serviceLocator.getSaveAndUpdateRecord().saveProductMedia(serviceLocator.getProductMappers().primaryProductBuilder(isProductMediaExisting, true));

        boolean cachePrimaryProductOnFalseResult  = serviceLocator.getRedisProductCacheRepo().saveUpdateProductMedia(serviceLocator.getProductMappers().toCacheProductMedia(savePrimaryProductOnFalse.getProductMedia()));
        boolean cachePrimaryProductOnTrueResult = serviceLocator.getRedisProductCacheRepo().saveUpdateProductMedia(serviceLocator.getProductMappers().toCacheProductMedia(savePrimaryProductOnTrue.getProductMedia()));

        handleCacheResult(cachePrimaryProductOnFalseResult, "false");
        handleCacheResult(cachePrimaryProductOnTrueResult, "true");

        return buildSuccessResponse(savePrimaryProductOnFalse.getProductMedia(), savePrimaryProductOnTrue.getProductMedia());
    }

    private ResponseEntity<PrimaryProductResModel> buildSuccessResponse(MediaDbModel cur, MediaDbModel  prev) {
        return new ResponseEntity<>(
                new PrimaryProductResModel(
                        true,
                        "Messaage",
                        cur.getProductId(),
                        cur.getMediaType(),
                        cur.getIsActive(),
                        new PrimaryProductResModel.IsTrue(
                                cur.getId(),
                                cur.getIsPrimary(),
                                cur.getMediaUrl(),
                                cur.getUpdatedAt(),
                                cur.getCreatedAt()
                        ),
                        new PrimaryProductResModel.IsFalse(
                                prev.getId(),
                                prev.getIsPrimary(),
                                prev.getMediaUrl(),
                                prev.getUpdatedAt(),
                                prev.getCreatedAt()
                        )
                ),
                HttpStatus.OK
        );
    }

    private void handleCacheResult(boolean cacheResult, String target) {
        if (!cacheResult && target.equalsIgnoreCase("false")) {
            System.out.println("dfjwdfjw bdcds");
        }

        if (!cacheResult && target.equalsIgnoreCase("true")) {
            System.out.println("dfjwdfjdcdschsw bdcds");
        }
    }


    private void validateRequestToken(String token) {
        serviceLocator.getValidationUtils().jwtValidateRequest(token);
    }

    private void validateRequestPrimaryListenBruceForce(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private MediaDbModel findByIdAndOrganisationId(Integer id, UUID organisationId) {
        return serviceLocator.getProductMediaRepo().findByIdAndOrganisationIdAndIsActive(id, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, "jjnjn", AppConfig.UPDATE_PRODUCT_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}
