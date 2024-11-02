package com.dart.product.service.product_comments;

import com.dart.product.dto_model.product_comments_model.*;
import com.dart.product.entity.product_comment_entity.ProductCommentDbModel;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductCommentRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class GetAllProductCommentsService {

    private final ProductCommentRepo productCommentRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    public GetAllProductCommentsService(
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

    public ResponseEntity<ProductCommentAllResDTO> getAllProductComment(String authToken, Integer productId) {

        validateRequestToken(authToken);
        validProductId(productId);

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validationUserRole(roles);
        validateBruteForceProtection(userId.toString());

        FetchAllProductCommentModel getCacheRecord = redisProductCacheRepo.findAllProductComment(organisationId.toString(), productId);

        if(getCacheRecord.getStatus()){
            List<ProductCommentDbModel> mapCacheToPersistence = productMappers.mapToAllProductComment(getCacheRecord.getProductComment());
            return new ResponseEntity<>(productMappers.allProductCommentResponseBuilder(mapCacheToPersistence, AppConfig.VALID_GET_PRODUCT_COMMENT_RESPONSE), HttpStatus.OK);
        }

        List<ProductCommentDbModel> getPersistedRecord = findByIdAndOrganisationIdAndIsActive(productId, organisationId);
        return new ResponseEntity<>(productMappers.allProductCommentResponseBuilder(getPersistedRecord, AppConfig.VALID_GET_PRODUCT_COMMENT_RESPONSE), HttpStatus.OK);
    }

    private void validProductId(Integer productId) {
        validationUtils.validProductId(productId);
    }


    private void validateRequestToken(String token) {
        validationUtils.jwtValidateRequest(token);
    }

    private void validationUserRole(String role) {
        validationUtils.userRoleValidation(role);
    }

    private void validateBruteForceProtection(String userId) {
        validationUtils.bruteForceProtection(AppConfig.GET_ALL_PRODUCT_COMMENT_BRUTE_FORCE_PROTECTION + userId);
    }

    private List<ProductCommentDbModel> findByIdAndOrganisationIdAndIsActive(Integer productId, UUID organisationId) {
        return productCommentRepo.findByProductIdAndOrganisationIdAndIsActive( productId, organisationId, true, Sort.by(Sort.Direction.ASC, "id"))
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.INVALID_PRODUCT_COMMENT_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}

