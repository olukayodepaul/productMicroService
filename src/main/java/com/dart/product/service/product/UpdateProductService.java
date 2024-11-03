package com.dart.product.service.product;


import com.dart.product.dto_model.product_dto_model.ProductReqDTO;
import com.dart.product.dto_model.product_dto_model.ProductResModelDTO;
import com.dart.product.entity.product_entity.ProductDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductsRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class UpdateProductService {

    private final ProductsRepo productsRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;
    private static final Logger logger = LoggerFactory.getLogger(CreateProductService.class);

    public UpdateProductService(
            ProductsRepo productsRepo,
            FilterService jwtService,
            UtilitiesManager utilitiesManager,
            ProductMappers productMappers,
            RedisProductCacheRepo redisProductCacheRepo,
            ValidationUtils validationUtils
    ) {
        this.productsRepo = productsRepo;
        this.jwtService = jwtService;
        this.utilitiesManager = utilitiesManager;
        this.productMappers = productMappers;
        this.redisProductCacheRepo = redisProductCacheRepo;
        this.validationUtils = validationUtils;
    }

    public ResponseEntity<ProductResModelDTO> updateProduct( String authToken, ProductReqDTO reqBody, Integer id) {

        validateRequestToken(authToken);
        validateRequestBody(reqBody);

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validateUserRole(roles);
        validateBruteForceProtection(userId.toString());

        ProductDbEntity existingProduct = findByIdAndOrganisationIdAndIsActive(id, organisationId);

        reqBody.setOrganisation_id(existingProduct.getOrganisationId());
        reqBody.setCreated_at(existingProduct.getCreated_at());
        reqBody.setUpdated_at(LocalDateTime.now());
        reqBody.set_active(existingProduct.getIsActive());
        reqBody.setId(existingProduct.getId());
        SaveAndUpdateProductResponse persistRecord = saveProductRecord(productMappers.toProduct(reqBody));

        checkIfRecordPersisted(persistRecord);

        boolean cacheRecord = redisProductCacheRepo.saveUpdateProduct(productMappers.toProductCache(persistRecord.getProduct()));

        checkIfRecordCached(cacheRecord);

        // TODO: Send newly created product to searchMicroService through (gRPC)
        return new ResponseEntity<>(productMappers.toProductResponseBuilder(persistRecord.getProduct(), AppConfig.UPDATE_PRODUCT_RESPONSE), HttpStatus.OK);

    }

    private void checkIfRecordCached(boolean isRecord) {
        if (!isRecord) {
            // Send failure notification through Kafka
        }
    }

    private void validateRequestToken(String token) {
        validationUtils.accessTokenValidation(token);
    }

    private void validateUserRole(String role) {
        validationUtils.adminRoleValidation(role);
    }

    private void validateBruteForceProtection(String uuid) {
        validationUtils.bruteForceProtection(AppConfig.UPDATE_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private void validateRequestBody(ProductReqDTO reqBody) {
        validationUtils.productValidateRequest(reqBody);
        validationUtils.validateProductRecord(reqBody);
    }

    private ProductDbEntity findByIdAndOrganisationIdAndIsActive(Integer id, UUID organisationId) {
        return productsRepo.findByIdAndOrganisationIdAndIsActive(id, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.UPDATE_RESOURCES_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    private void checkIfRecordPersisted(SaveAndUpdateProductResponse isSave) {
        if (!isSave.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public SaveAndUpdateProductResponse saveProductRecord(ProductDbEntity regDetails) {
        try {
            return new SaveAndUpdateProductResponse(true, "", productsRepo.save(regDetails)) ;
        } catch (Exception e) {
            logger.error("UpdateProductService::saveProductRecord: {}", e.getMessage());
            return new SaveAndUpdateProductResponse(false, e.getMessage(), ProductDbEntity.builder().build());
        }
    }

}
