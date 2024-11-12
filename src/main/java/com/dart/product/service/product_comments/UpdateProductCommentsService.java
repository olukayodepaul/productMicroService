package com.dart.product.service.product_comments;


import com.dart.product.dto_model.product_comments_model.AddProductCommentReqlDTO;
import com.dart.product.dto_model.product_comments_model.ProductCommentResDTO;
import com.dart.product.dto_model.product_comments_model.SaveAndUpdateProductCommentResponse;
import com.dart.product.entity.product_comment_entity.ProductCommentDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductCommentRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class UpdateProductCommentsService {

    private final ProductCommentRepo productCommentRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    public UpdateProductCommentsService(
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

    public ResponseEntity<ProductCommentResDTO> updateProductComment(
            String authToken, AddProductCommentReqlDTO reqBody, Integer productId, Integer id) {

        validateRequestToken(authToken);
        validProductId(productId);
        validCommentId(id);
        validateRequestBody(reqBody);

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validationUserRole(roles);
        validateBruteForceProtection(userId.toString());

        ProductCommentDbEntity existingProductComment = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);

        reqBody.setOrganisation_id(existingProductComment.getOrganisationId());
        reqBody.setCreated_at(existingProductComment.getCreatedAt());
        reqBody.setUpdated_at(LocalDateTime.now());
        reqBody.set_active(existingProductComment.isActive());
        reqBody.setUser_id(existingProductComment.getUserId().toString());
        reqBody.setProduct_id(existingProductComment.getProductId());
        reqBody.setId(existingProductComment.getId());
        SaveAndUpdateProductCommentResponse persistRecord = saveProductComment(productMappers.mapAddProductCommentModelToDbModel(reqBody));

        checkIfRecordPersisted(persistRecord);

        boolean cacheRecordInMemory = redisProductCacheRepo.saveUpdateProductComment(productMappers.mapProductCommentDbModelToDbModel(persistRecord.getProductComments()));

        isRecordSaveInTheCache(cacheRecordInMemory);

        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
        return new ResponseEntity<>(productMappers.productCommentResponseBuilder(persistRecord.getProductComments(), "product comment successfully updated"), HttpStatus.OK);

    }

    private void isRecordSaveInTheCache(boolean isRecord) {
        if(!isRecord){
            //send through kafka
        }
    }

    private void validateRequestBody(AddProductCommentReqlDTO reqBody) {
        validationUtils.productCommentValidateRequest(reqBody);
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
        validationUtils.bruteForceProtection(AppConfig.UPDATE_PRODUCT_COMMENT_BRUTE_FORCE_PROTECTION + userId);
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
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.UPDATE_RESOURCES_RESPONSE),
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

