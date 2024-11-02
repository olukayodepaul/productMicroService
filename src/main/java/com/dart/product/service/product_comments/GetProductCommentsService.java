package com.dart.product.service.product_comments;


import com.dart.product.dto_model.product_comments_model.FetchOneProductCommentModel;
import com.dart.product.dto_model.product_comments_model.ProductCommentOneResDTO;
import com.dart.product.entity.product_comment_entity.ProductCommentDbModel;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductCommentRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
public class GetProductCommentsService {

    private final ProductCommentRepo productCommentRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    public GetProductCommentsService(
            ProductCommentRepo productCommentRepo,
            FilterService jwtService,
            UtilitiesManager utilitiesManager,
            ProductMappers productMappers,
            RedisProductCacheRepo redisProductCacheRepo,
            ValidationUtils validationUtils
    ) {
        this.productCommentRepo = productCommentRepo;
        this.jwtService = jwtService;
        this.utilitiesManager = utilitiesManager;
        this.productMappers = productMappers;
        this.redisProductCacheRepo = redisProductCacheRepo;
        this.validationUtils = validationUtils;
    }

    public ResponseEntity<ProductCommentOneResDTO> getProductComment(String authToken,  Integer productId, Integer id) {

        validateRequestToken(authToken);
        validProductId(productId);
        validCommentId(id);

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validationUserRole(roles);
        validateBruteForceProtection(userId.toString());

        FetchOneProductCommentModel getCacheRecord = redisProductCacheRepo.findOneProductComment(organisationId.toString(), productId, id);

        if(getCacheRecord.getStatus()){
            ProductCommentDbModel mapCacheToPersistence = productMappers.mapDbModelToProductCommentDbModel(getCacheRecord.getProductComment());
            return new ResponseEntity<>(productMappers.productCommentResponseBuilder(mapCacheToPersistence, AppConfig.VALID_GET_PRODUCT_COMMENT_RESPONSE), HttpStatus.OK);
        }

        ProductCommentDbModel getPersistedRecord = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);
        return new ResponseEntity<>(productMappers.productCommentResponseBuilder(getPersistedRecord, AppConfig.VALID_GET_PRODUCT_COMMENT_RESPONSE), HttpStatus.OK);

    }

    private void validProductId(Integer productId) {
        validationUtils.validProductId(productId);
    }

    private void validCommentId(Integer id) {
        validationUtils.validCommentId(id);
    }

    private void validateRequestToken(String token) {
        validationUtils.jwtValidateRequest(token);
    }

    private void validationUserRole(String role) {
        validationUtils.userRoleValidation(role);
    }

    private void validateBruteForceProtection(String userId) {
        validationUtils.bruteForceProtection(AppConfig.GET_PRODUCT_COMMENT_BRUTE_FORCE_PROTECTION + userId);
    }

    private ProductCommentDbModel findByIdAndOrganisationIdAndIsActive(Integer id, Integer productId, UUID organisationId) {
        return productCommentRepo.findByIdAndProductIdAndOrganisationIdAndIsActive(id,  productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}

