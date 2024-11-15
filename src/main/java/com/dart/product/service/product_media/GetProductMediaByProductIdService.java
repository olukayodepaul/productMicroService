package com.dart.product.service.product_media;

import com.dart.product.di.ServicesDi;
import com.dart.product.dto_model.product_media_model.FetchAllProductMediaModel;
import com.dart.product.dto_model.product_media_model.GetAllMediaDTO;
import com.dart.product.entity.prodct_media.MediaContentDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductMediaContentRepo;
import com.dart.product.repository.ProductMediaRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;


@Service
public class GetProductMediaByProductIdService {

    private static final Logger logger = LoggerFactory.getLogger(GetProductMediaByProductIdService.class);
    private final ProductMediaContentRepo productMediaContentRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    public GetProductMediaByProductIdService(
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


    public ResponseEntity<GetAllMediaDTO> getProductMediaByProductId(String authToken, Integer productId) {

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        validateBruteForceProtection(userId.toString());
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validateRequestToken(authToken);
        validProductId(productId);
        validateUserRole(roles);

        FetchAllProductMediaModel cachedProductMedia = redisProductCacheRepo.findAllProductMedia(organisationId.toString(), productId.toString());

        if (cachedProductMedia.getStatus()) {
            List<MediaContentDbEntity> mapResult = productMappers.mapProductMediaCacheToProductDTO(cachedProductMedia.getProductMedia());
            return new ResponseEntity<>(productMappers.mapProductMediaEntityProductDTO(mapResult, AppConfig.PRODUCT_MEDIA_FETCH_RESPONSE), HttpStatus.OK);
        }

        List<MediaContentDbEntity> getPersistedProduct = getPersistedProductMedia(productId, organisationId);
        return new ResponseEntity<>(productMappers.mapProductMediaEntityProductDTO(getPersistedProduct, AppConfig.PRODUCT_MEDIA_FETCH_RESPONSE), HttpStatus.OK);

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
        validationUtils.bruteForceProtection(AppConfig.FETCH_PRIMARY_PRODUCT_MEDIA_BY_PRODUCT_ID_BRUTE_FORCE_PROTECTION + userId);
    }

    private List<MediaContentDbEntity> getPersistedProductMedia(Integer productId, UUID organisationId) {
        return productMediaContentRepo.findByProductIdAndOrganisationIdAndIsActiveOrderByIdAsc(productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(

                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.INVALID_RESOURCES_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}
