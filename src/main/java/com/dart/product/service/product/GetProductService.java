package com.dart.product.service.product;

import com.dart.product.entity.product_entity.ProductDbEntity;
import com.dart.product.dto_model.product_dto_model.*;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductsRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetProductService {

    private final ProductsRepo productsRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    public GetProductService(
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

    public ResponseEntity<ProductResModelDTO> retrieveProduct(String authToken, Integer id) {

        validateRequestToken(authToken);
        validateProductId(id);

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validateUserRole(roles);
        validateBruteForceProtection(userId.toString());

        FetchProductsResModel getCacheProduct = redisProductCacheRepo.getProducts(organisationId.toString(), id);

        if (getCacheProduct.getStatus()) {
            ProductDbEntity mapCacheToPersistence = productMappers.mapProductCacheToPersistence(getCacheProduct.getProducts());
            return new ResponseEntity<>(productMappers.toProductResponseBuilder(mapCacheToPersistence, AppConfig.GET_PRODUCT_RESPONSE), HttpStatus.OK);
        }

        ProductDbEntity getPersistedProduct = findByIdAndOrganisationIdAndIsActive(id, organisationId);
        return new ResponseEntity<>(productMappers.toProductResponseBuilder(getPersistedProduct, AppConfig.GET_PRODUCT_RESPONSE), HttpStatus.OK);
    }

    private void validateUserRole(String role) {
        validationUtils.adminRoleValidation(role);
    }

    private void validateBruteForceProtection(String uuid) {
        validationUtils.bruteForceProtection(AppConfig.GET_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
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

}
