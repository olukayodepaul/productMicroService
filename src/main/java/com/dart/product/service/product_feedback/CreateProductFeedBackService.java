package com.dart.product.service.product_feedback;


import com.dart.product.dto_model.product_feedback.AddProductFeedBackReqDTO;
import com.dart.product.dto_model.product_feedback.ProductFeedBackResDTO;
import com.dart.product.dto_model.product_feedback.SaveAndUpdateProductFeedBackResponse;
import com.dart.product.entity.product_feedback_entity.ProductFeedBackDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductFeedBackRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;



@Service
public class CreateProductFeedBackService {

    private final ProductFeedBackRepo productFeedBackRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    public CreateProductFeedBackService(
            ProductFeedBackRepo productFeedBackRepo,
            FilterService jwtService,
            UtilitiesManager utilitiesManager,
            ProductMappers productMappers,
            RedisProductCacheRepo redisProductCacheRepo,
            ValidationUtils validationUtils
    ) {
        this.productFeedBackRepo = productFeedBackRepo;
        this.jwtService = jwtService;
        this.utilitiesManager = utilitiesManager;
        this.productMappers = productMappers;
        this.redisProductCacheRepo = redisProductCacheRepo;
        this.validationUtils = validationUtils;
    }

    public ResponseEntity<ProductFeedBackResDTO> createProductFeedBack(String authToken, AddProductFeedBackReqDTO reqBody, Integer productId) {

        validateRequestToken(authToken);
        validProductId(productId);
        validateRequestBody(reqBody);

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validationUserRole(roles);
        validateBruteForceProtection(userId.toString());

        Optional<ProductFeedBackDbEntity> isUserPresent =  productFeedBackRepo.findByProductIdAndOrganisationIdAndUserId(productId, organisationId, userId);

        reqBody.setId(0);
        reqBody.setCreated_at(LocalDateTime.now());

        if(isUserPresent.isPresent()){
            reqBody.setId(isUserPresent.get().getId());
            reqBody.setCreated_at(isUserPresent.get().getCreatedAt());
        }

        reqBody.setOrganisation_id(organisationId);
        reqBody.setUpdated_at(LocalDateTime.now());
        reqBody.setUser_id(userId.toString());
        reqBody.setProduct_id(productId);

        SaveAndUpdateProductFeedBackResponse persistRecord = saveProductFeedBack(productMappers.mapAddProductFeedBackModelToDbModel(reqBody));

        checkIfRecordPersisted(persistRecord);

        boolean cacheRecord = redisProductCacheRepo.saveUpdateProductFeedBack(productMappers.mapProductFeedBackDbModelToDbModel(persistRecord.getProductFeedback()));

        isRecordSaveInTheCache(cacheRecord);

        return new ResponseEntity<>(
                productMappers.productFeedBackResponseBuilder(
                        persistRecord.getProductFeedback(), AppConfig.PRODUCT_FEEDBACK_SUCCESSFULLY_CREATED),
                HttpStatus.CREATED);
    }

    private void validProductId(Integer productId) {
        validationUtils.validProductId(productId);
    }

    private void isRecordSaveInTheCache(boolean isRecord) {
        if (!isRecord) {
            publishKafkaMessage("ProductFeedbackCacheFailure", "Failed to save feedback in cache for product ID: ");
        }
    }

    private void publishKafkaMessage(String topic, String message) {
        // Logic for publishing a message to Kafka
    }

    private void validateRequestBody(AddProductFeedBackReqDTO reqBody) {
        validationUtils.productFeedBackValidateRequest(reqBody);
    }

    private void validateRequestToken(String token) {
        validationUtils.jwtValidateRequest(token);
    }

    private void validationUserRole(String role) {
        validationUtils.userRoleValidation(role);
    }

    private void validateBruteForceProtection(String userId) {
        validationUtils.bruteForceProtection(AppConfig.CREATE_PRODUCT_FEEDBACK_BRUTE_FORCE_PROTECTION + userId);
    }

    private void checkIfRecordPersisted(SaveAndUpdateProductFeedBackResponse isSave) {
        if (!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private SaveAndUpdateProductFeedBackResponse saveProductFeedBack(ProductFeedBackDbEntity regDetails) {
        try {
            return new SaveAndUpdateProductFeedBackResponse(true, "", productFeedBackRepo.save(regDetails));
        } catch (Exception e) {
            return new SaveAndUpdateProductFeedBackResponse(false, e.getMessage(), ProductFeedBackDbEntity.builder().build());
        }
    }

}
