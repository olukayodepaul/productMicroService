package com.dart.product.service.product;

import com.dart.product.entity.product_entity.ProductDbEntity;
import com.dart.product.dto_model.product_dto_model.*;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductsRepo;
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
public class GetAllProductService {

    private final ProductsRepo productsRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    public GetAllProductService(
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


    public ResponseEntity<AllProductResDto> retrieveAllProduct(String authToken)
    {

        validateRequestToken(authToken);

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validateUserRole(roles);
        validateBruteForceProtection(userId.toString());

        FetchAllProductsResModel getAllCacheProduct = redisProductCacheRepo.getAllProducts(organisationId.toString());

        if(getAllCacheProduct.getStatus()){

            List<ProductDbEntity> mapCacheToPersistence = productMappers.toCacheFromProduct(getAllCacheProduct.getProducts());
            return new ResponseEntity<>(productMappers.toAllProductResponseBuilder(mapCacheToPersistence, AppConfig.GET_PRODUCT_RESPONSE), HttpStatus.OK);

        }

        List<ProductDbEntity> getPersistedProduct = findByOrganisationIdAndIsActive(organisationId);
        return new ResponseEntity<>(productMappers.toAllProductResponseBuilder(getPersistedProduct, AppConfig.GET_PRODUCT_RESPONSE), HttpStatus.OK);

    }

    private void validateRequestToken(String token) {
        validationUtils.accessTokenValidation(token);
    }

    private void validateUserRole(String role) {
        validationUtils.adminRoleValidation(role);
    }

    private void validateBruteForceProtection(String uuid) {
        validationUtils.bruteForceProtection(AppConfig.GET_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private List<ProductDbEntity> findByOrganisationIdAndIsActive(UUID organisationId) {
        return productsRepo.findByOrganisationIdAndIsActive(organisationId, true, Sort.by(Sort.Direction.ASC, "id"))
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.PRODUCT_NOT_FOUND_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
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
