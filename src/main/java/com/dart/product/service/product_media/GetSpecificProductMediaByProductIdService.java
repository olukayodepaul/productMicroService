package com.dart.product.service.product_media;


import com.dart.product.dependency.di.ServicesDi;
import com.dart.product.dto_model.product_media_model.FetchAllProductMediaModel;
import com.dart.product.dto_model.product_media_model.GetSpecMediaDTO;
import com.dart.product.entity.prodct_media.MediaContentDbEntity;
import com.dart.product.entity.prodct_media.ProductContentMediaCacheEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductMediaContentRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;


@Service
public class GetSpecificProductMediaByProductIdService {

    private static final Logger logger = LoggerFactory.getLogger(GetSpecificProductMediaByProductIdService.class);
    private final ProductMediaContentRepo productMediaContentRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    public GetSpecificProductMediaByProductIdService(
            ProductMediaContentRepo productMediaContentRepo,
            ServicesDi servicesDi
    )
    {
        this.productMediaContentRepo = productMediaContentRepo;
        this.jwtService = servicesDi.jwtService();
        this.utilitiesManager = servicesDi.utilitiesManager();
        this.productMappers = servicesDi.productMappers();
        this.redisProductCacheRepo = servicesDi.redisProductCacheRepo();
        this.validationUtils = servicesDi.validationUtils();
    }


    public ResponseEntity<GetSpecMediaDTO> getSpecificProductMediaMediaType(String authToken, Integer productId, String mediaType) {

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        validateBruteForceProtection(userId.toString());
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validateRequestToken(authToken);
        validProductId(productId);
        validMediaType(mediaType);
        validateUserRole(roles);

        FetchAllProductMediaModel cachedProductMedia = redisProductCacheRepo.findAllProductMedia(organisationId.toString(), productId.toString());

        if (cachedProductMedia.getStatus()) {
            List<ProductContentMediaCacheEntity> filter = cachedProductMedia.getProductMedia().stream().filter(
                            filters -> filters.getMedia_type().equalsIgnoreCase(mediaType)
                    )
                    .sorted(Comparator.comparing(ProductContentMediaCacheEntity::getId))
                    .toList();
            return new ResponseEntity<>(productMappers.getAllSpecificMedia(filter, AppConfig.PRODUCT_MEDIA_FETCH_RESPONSE), HttpStatus.OK);
        }

        List<MediaContentDbEntity> getPersistedProduct = getPersistedProductMedia(mediaType, productId, organisationId);
        return new ResponseEntity<>(productMappers.getAllSpecificMedia(productMappers.mapProductMedia(getPersistedProduct), AppConfig.PRODUCT_MEDIA_FETCH_RESPONSE), HttpStatus.OK);

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
        validationUtils.bruteForceProtection(AppConfig.FETCH_SPECIFIC_PRIMARY_PRODUCT_MEDIA_BRUTE_FORCE_PROTECTION + userId);
    }

    private void validMediaType(String mediaType) {
        validationUtils.mediaTypeValidation(mediaType);
    }

    private List<MediaContentDbEntity> getPersistedProductMedia(String mediaType, Integer productId, UUID organisationId) {
        return productMediaContentRepo.findByProductIdAndOrganisationIdAndMediaTypeAndIsActiveOrderByIdAsc(productId, organisationId, mediaType, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.INVALID_RESOURCES_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}
