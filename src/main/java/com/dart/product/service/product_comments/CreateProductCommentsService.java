package com.dart.product.service.product_comments;

import com.dart.product.dependency.di.ServicesDi;
import com.dart.product.dto_model.product_comments_model.AddProductCommentReqlDTO;
import com.dart.product.entity.product_comment_entity.ProductCommentDbEntity;
import com.dart.product.dto_model.product_comments_model.ProductCommentResDTO;
import com.dart.product.dto_model.product_comments_model.SaveAndUpdateProductCommentResponse;
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
public class CreateProductCommentsService {

    private final ProductCommentRepo productCommentRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    private static final Logger logger = LoggerFactory.getLogger(CreateProductService.class);

    public CreateProductCommentsService(ProductCommentRepo productCommentRepo, ServicesDi servicesDi) {
        this.productCommentRepo = productCommentRepo;
        this.jwtService = servicesDi.jwtService();
        this.utilitiesManager = servicesDi.utilitiesManager();
        this.productMappers = servicesDi.productMappers();
        this.redisProductCacheRepo = servicesDi.redisProductCacheRepo();
        this.validationUtils = servicesDi.validationUtils();
    }

    public ResponseEntity<ProductCommentResDTO> createProductComment(String authToken, AddProductCommentReqlDTO reqBody, Integer productId) {

        validateRequestToken(authToken);
        validProductId(productId);
        validateRequestBody(reqBody);

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validationUserRole(roles);
        validateBruteForceProtection(userId.toString());

        reqBody.setOrganisation_id(organisationId);
        reqBody.setCreated_at(LocalDateTime.now());
        reqBody.setUpdated_at(LocalDateTime.now());
        reqBody.set_active(true);
        reqBody.setUser_id(userId.toString());
        reqBody.setProduct_id(productId);
        reqBody.setId(0);
        SaveAndUpdateProductCommentResponse persistRecord = saveProductComment(productMappers.mapAddProductCommentModelToDbModel(reqBody));

        checkIfRecordPersisted(persistRecord);

        boolean cacheRecord = redisProductCacheRepo.saveUpdateProductComment(productMappers.mapProductCommentDbModelToDbModel(persistRecord.getProductComments()));

        checkIfRecordCached(cacheRecord);

        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
        return new ResponseEntity<>(productMappers.productCommentResponseBuilder(persistRecord.getProductComments(), AppConfig.PRODUCT_COMMENT_SUCCESSFULLY_CREATED), HttpStatus.CREATED);

    }

    private void checkIfRecordCached(boolean isRecord) {
        if(!isRecord){
            //send through kafka
        }
    }

    private void checkIfRecordPersisted(SaveAndUpdateProductCommentResponse isSave) {
        if(!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validProductId(Integer productId) {
        validationUtils.validProductId(productId);
    }

    private void validateRequestBody(AddProductCommentReqlDTO reqBody) {
        validationUtils.productCommentValidateRequest(reqBody);
    }

    private void validateRequestToken(String token) {
        validationUtils.jwtValidateRequest(token);
    }

    private void validationUserRole(String role) {
        validationUtils.userRoleValidation(role);
    }

    private void validateBruteForceProtection(String userId) {
        validationUtils.bruteForceProtection(AppConfig.CREATE_PRODUCT_COMMENT_BRUTE_FORCE_PROTECTION + userId);
    }

    private SaveAndUpdateProductCommentResponse saveProductComment(ProductCommentDbEntity regDetails) {
        try {
            return new SaveAndUpdateProductCommentResponse(true, "", productCommentRepo.save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateProductCommentResponse(false, e.getMessage(), ProductCommentDbEntity.builder().build());
        }
    }

}

