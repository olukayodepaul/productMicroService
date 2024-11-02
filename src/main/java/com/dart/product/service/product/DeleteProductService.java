package com.dart.product.service.product;


import com.dart.product.dto_model.product_dto_model.ProductResModelDTO;
import com.dart.product.entity.product_entity.ProductDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductsRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeleteProductService {

    private final ProductsRepo productsRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    public DeleteProductService(
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

    public ResponseEntity<ProductResModelDTO> deleteProduct(String authToken, Integer id)
    {

        validateRequestToken(authToken);
        validateProductId(id);

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validateUserRole(roles);
        validateBruteForceProtection(userId.toString());

        ProductDbEntity existingProduct = findByIdAndOrganisationIdAndIsActive(id, organisationId);

        existingProduct.setUpdated_at(LocalDateTime.now());
        existingProduct.setIsActive(false);
        SaveAndUpdateProductResponse persistRecord = saveProductRecord(existingProduct);

        isPersistedRecordDeleted(persistRecord);

        boolean deleteCacheRecord = redisProductCacheRepo.deleteProduct(organisationId.toString(), id);

        isCacheRecordDeleted(deleteCacheRecord);

        // TODO: Send newly created product to searchMicroService through (gRPC)
        return new ResponseEntity<>(productMappers.toProductResponseBuilder(persistRecord.getProduct(), AppConfig.DELETE_PRODUCT_RESPONSE), HttpStatus.OK);
    }

    private void isCacheRecordDeleted(boolean isRecord) {
        if (!isRecord) {
            // Send failure notification through Kafka
        }
    }

    private void validateUserRole(String role) {
        validationUtils.adminRoleValidation(role);
    }

    private void validateBruteForceProtection(String uuid) {
        validationUtils.bruteForceProtection(AppConfig.DELETE_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private void validateRequestToken(String token) {
        validationUtils.accessTokenValidation(token);
    }

    private void validateProductId(Integer id) {
        validationUtils.validateProductId(id);
    }

    private ProductDbEntity findByIdAndOrganisationIdAndIsActive(Integer id, UUID organisationId) {
        return productsRepo.findByIdAndOrganisationIdAndIsActive(id, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.PRODUCT_NOT_FOUND_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    private void isPersistedRecordDeleted(SaveAndUpdateProductResponse isSave) {
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
            //logger.error("DbSaveUpdatedService::updateProductRecord: {}", e.getMessage());
            return new SaveAndUpdateProductResponse(false, e.getMessage(), ProductDbEntity.builder().build());
        }
    }

}
