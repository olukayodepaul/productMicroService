package com.dart.product.service.product_media;


import com.dart.product.dto_model.product_media_model.FetchProductMediaModel;
import com.dart.product.dto_model.product_media_model.ProductMediaResDTO;
import com.dart.product.entity.prodct_media.MediaDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductMediaRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
public class GetProductMediaByIdService {

    private final ProductMappers productMappers;
    private final UtilitiesManager utilitiesManager;
    private final ValidationUtils validationUtils;
    private final FilterService jwtService;
    private final ProductMediaRepo productMediaRepo;
    private final RedisProductCacheRepo redisProductCacheRepo;

    public GetProductMediaByIdService(
            UtilitiesManager utilitiesManager,
            ValidationUtils validationUtils,
            FilterService jwtService,
            ProductMappers productMappers,
            ProductMediaRepo productMediaRepo,
            RedisProductCacheRepo redisProductCacheRepo
    ) {
        this.utilitiesManager = utilitiesManager;
        this.validationUtils = validationUtils;
        this.jwtService = jwtService;
        this.productMappers = productMappers;
        this.productMediaRepo = productMediaRepo;
        this.redisProductCacheRepo = redisProductCacheRepo;
    }

    public ResponseEntity<ProductMediaResDTO> getProductMediaByMediaId(String authToken, Integer productId, Integer mediaId) {

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        validateBruteForceProtection(userId.toString());
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validateRequestToken(authToken);
        validProductId(productId);
        validMediaId(mediaId);
        validateUserRole(roles);

        FetchProductMediaModel cachedProductMedia = redisProductCacheRepo.findOneProductMedia(organisationId.toString(), productId, mediaId);

        if(cachedProductMedia.getStatus()) {
            return new ResponseEntity<>(productMappers.toProductMediaToResDTO(cachedProductMedia.getProductMedia(), AppConfig.PRODUCT_MEDIA_FETCH_RESPONSE), HttpStatus.OK);
        }

        MediaDbEntity getPersistedProduct = getPersistedProductMedia(mediaId, productId, organisationId);
        return new ResponseEntity<>(productMappers.toProductMediaResponse(getPersistedProduct, AppConfig.PRODUCT_MEDIA_FETCH_RESPONSE), HttpStatus.OK);

    }

    private void validateRequestToken(String token) {
        validationUtils.accessTokenValidation(token);
    }

    private void validateUserRole(String role) {
        validationUtils.adminRoleValidation(role);
    }

    private void validProductId(Integer productId) {
        validationUtils.validProductId(productId);
    }

    private void validateBruteForceProtection(String userId) {
        validationUtils.bruteForceProtection(AppConfig.FETCH_ONE_PRIMARY_PRODUCT_MEDIA_BRUTE_FORCE_PROTECTION + userId);
    }

    private void validMediaId(Integer mediaId) {
        validationUtils.mediaIdValidation(mediaId);
    }

    private MediaDbEntity getPersistedProductMedia(Integer mediaId, Integer productId, UUID organisationId) {
        return productMediaRepo.findByIdAndProductIdAndOrganisationIdAndIsActive(mediaId, productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(

                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.INVALID_RESOURCES_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}
