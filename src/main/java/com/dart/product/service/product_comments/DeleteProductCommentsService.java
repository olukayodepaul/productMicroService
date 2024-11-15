package com.dart.product.service.product_comments;

import com.dart.product.di.ServicesDi;
import com.dart.product.dto_model.product_comments_model.ProductCommentResDTO;
import com.dart.product.dto_model.product_comments_model.SaveAndUpdateProductCommentResponse;
import com.dart.product.entity.product_comment_entity.ProductCommentDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductCommentRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.service.product.CreateProductService;
import com.dart.product.utilities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeleteProductCommentsService {

    private final ProductCommentRepo productCommentRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    private static final Logger logger = LoggerFactory.getLogger(DeleteProductCommentsService.class);

    public DeleteProductCommentsService(ProductCommentRepo productCommentRepo, ServicesDi servicesDi) {
        this.productCommentRepo = productCommentRepo;
        this.jwtService = servicesDi.jwtService();
        this.utilitiesManager = servicesDi.utilitiesManager();
        this.productMappers = servicesDi.productMappers();
        this.redisProductCacheRepo = servicesDi.redisProductCacheRepo();
        this.validationUtils = servicesDi.validationUtils();
    }

    public ResponseEntity<ProductCommentResDTO> deleteProductComment(
            String authToken,  Integer productId, Integer id) {

        validateRequestToken(authToken);
        validProductId(productId);
        validCommentId(id);

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validationUserRole(roles);
        validateBruteForceProtection(userId.toString());

        ProductCommentDbEntity existingProductComment = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);

        existingProductComment.setUpdatedAt(LocalDateTime.now());
        existingProductComment.setActive(false);
        SaveAndUpdateProductCommentResponse persistRecord = saveProductComment(existingProductComment);

        checkIfRecordPersisted(persistRecord);

        boolean deleteCacheRecord = redisProductCacheRepo.deleteProductComment(productMappers.mapProductCommentDbModelToDbModel(persistRecord.getProductComments()));
        isCacheRecordDeleted(deleteCacheRecord);

        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
        return new ResponseEntity<>(productMappers.productCommentResponseBuilder(persistRecord.getProductComments(), AppConfig.PRODUCT_COMMENT_SUCCESSFULLY_DELETED), HttpStatus.OK);

    }

    private void isCacheRecordDeleted(boolean isRecord) {
        if(!isRecord){
            //send through kafka
        }
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
        validationUtils.bruteForceProtection(AppConfig.DELETE_PRODUCT_COMMENT_BRUTE_FORCE_PROTECTION + userId);
    }

    private void checkIfRecordPersisted(SaveAndUpdateProductCommentResponse isSave) {
        if(!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private ProductCommentDbEntity findByIdAndOrganisationIdAndIsActive(Integer id, Integer productId, UUID organisationId) {
        return productCommentRepo.findByIdAndProductIdAndOrganisationIdAndIsActive(id,  productId, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETE_RESOURCES_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    private SaveAndUpdateProductCommentResponse saveProductComment(ProductCommentDbEntity regDetails) {
        try {
            return new SaveAndUpdateProductCommentResponse(true, "", productCommentRepo.save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateProductCommentResponse(false, e.getMessage(), ProductCommentDbEntity.builder().build());
        }
    }

}

