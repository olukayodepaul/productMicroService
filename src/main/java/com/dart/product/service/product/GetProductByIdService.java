package com.dart.product.service.product;

import com.dart.product.entity.product_model.*;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductsRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetProductByIdService {

    private final UtilitiesManager utilitiesManager;
    private final ValidationUtils validationUtils;
    private final FilterService jwtService;
    private final ProductMappers mappers;
    private RedisProductCacheRepo redisCacheRepo;
    private ProductsRepo productsRepo;

    public GetProductByIdService(
            UtilitiesManager utilitiesManager,
            ValidationUtils validationUtils,
            FilterService jwtService,
            ProductMappers mappers,
            RedisProductCacheRepo redisCacheRepo,
            ProductsRepo productsRepo
    )
    {
        this.utilitiesManager = utilitiesManager;
        this.validationUtils = validationUtils;
        this.jwtService = jwtService;
        this.mappers = mappers;
        this.redisCacheRepo = redisCacheRepo;
        this.productsRepo = productsRepo;
    }

    public ResponseEntity<ProductResModel> getProductById(String token, Integer id)
    {

        String jwtToken = jwtService.extractTokenFromHeader(token);
        String roles = jwtService.extractRole(jwtToken);
        String plainUUID = jwtService.extractUUID(jwtToken);
        UUID organisationId =  utilitiesManager.convertStringToUUID(jwtService.extractUUID(jwtToken)); //is a uuid

        validateRequest(token, plainUUID, roles, id.toString());

        FetchProductsResModel getCacheRecord = redisCacheRepo.getProducts(organisationId.toString(), id);

        if(getCacheRecord.getStatus()) {

            System.out.println(1);
            return new ResponseEntity<>(new ProductResModel(
                    true,
                    "message",
                    mappers.toProductCacheResponse(getCacheRecord.getProducts())
            ), HttpStatus.OK);

        }else{

            System.out.println(2);
            ProductDbModel getProductRecord = fetchProduct(organisationId, id);
//            redisCacheRepo.saveUpdateProduct(mappers.toProductCache(getProductRecord));

            return new ResponseEntity<>(new ProductResModel(
                    true,
                    "message",
                    mappers.toProductResponse(getProductRecord)
            ), HttpStatus.OK);
        }
    }

    // organisationId,  true
    private ProductDbModel fetchProduct(UUID organisationId, Integer product_id) {
        return productsRepo.findById(product_id)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, AppConfig.GET_ALL_PRODUCT_ERROR_TAG, AppConfig.FETCH_ALL_PRODUCT_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    private void validateRequest(String token,  String uuid, String role, String id) {
//        validationUtils.userRoleValidateRequest(role);
        validationUtils.jwtValidateRequest(token);
//        validationUtils.roleValidation(role);
        validationUtils.IdValidation(id);
        validationUtils.bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }
}
