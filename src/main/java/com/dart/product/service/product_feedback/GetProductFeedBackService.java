package com.dart.product.service.product_feedback;


import com.dart.product.di.ServicesDi;
import com.dart.product.dto_model.product_feedback.FetchProductFeedBackModel;
import com.dart.product.dto_model.product_feedback.ProductFeedBackResDTO;
import com.dart.product.entity.product_feedback_entity.ProductFeedBackDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductFeedBackRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
public class GetProductFeedBackService {

    private final ProductFeedBackRepo productFeedBackRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    private static final Logger logger = LoggerFactory.getLogger(GetProductFeedBackService.class);

    public GetProductFeedBackService(ProductFeedBackRepo productFeedBackRepo, ServicesDi servicesDi) {
        this.productFeedBackRepo = productFeedBackRepo;
        this.jwtService = servicesDi.jwtService();
        this.utilitiesManager = servicesDi.utilitiesManager();
        this.productMappers = servicesDi.productMappers();
        this.redisProductCacheRepo = servicesDi.redisProductCacheRepo();
        this.validationUtils = servicesDi.validationUtils();
    }

    public ResponseEntity<ProductFeedBackResDTO> getProductFeedBack(String authToken, Integer productId) {

        validateRequestToken(authToken);
        validProductId(productId);

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validationUserRole(roles);
        validateBruteForceProtection(userId.toString());

        FetchProductFeedBackModel getCacheRecord = redisProductCacheRepo.findOneProductFeedBack(organisationId.toString(), productId);

        if(getCacheRecord.getStatus()) {
            ProductFeedBackDbEntity mapCacheToPersistence = productMappers.mapDbModelToProductFeedBackDbModel(getCacheRecord.getProductFeedBack());
            return new ResponseEntity<>(productMappers.productFeedBackResponseBuilder(mapCacheToPersistence, AppConfig.VALID_GET_PRODUCT_FEED_BACK_RESPONSE), HttpStatus.OK);
        }

        ProductFeedBackDbEntity getPersistedRecord = findByIdAndOrganisationIdAndIsActive(userId,  productId, organisationId);
        return new ResponseEntity<>(productMappers.productFeedBackResponseBuilder(getPersistedRecord, AppConfig.VALID_GET_PRODUCT_FEED_BACK_RESPONSE), HttpStatus.OK);

    }

    private void validateRequestToken(String token) {
        validationUtils.jwtValidateRequest(token);
    }

    private void validationUserRole(String role) {
        validationUtils.userRoleValidation(role);
    }

    private void validateBruteForceProtection(String uuid) {
        validationUtils.bruteForceProtection(AppConfig.GET_PRODUCT_FEEDBACK_BRUTE_FORCE_PROTECTION + uuid);
    }

    private void validProductId(Integer productId) {
        validationUtils.validProductId(productId);
    }

    private ProductFeedBackDbEntity findByIdAndOrganisationIdAndIsActive(UUID userId, Integer productId, UUID organisationId) {
        return productFeedBackRepo.findByProductIdAndOrganisationIdAndUserId(productId, organisationId, userId)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.INVALID_RESOURCES_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }


}
