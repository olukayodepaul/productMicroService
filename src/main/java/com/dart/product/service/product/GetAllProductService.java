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
public class GetAllProductService {

    private final UtilitiesManager utilitiesManager;
    private final ValidationUtils validationUtils;
    private final FilterService jwtService;
    private final ProductMappers mappers;
    private final SaveAndUpdateRecord saveAndUpdateRecord;
    private RedisProductCacheRepo redisCacheRepo;
    private ProductsRepo productsRepo;

    public GetAllProductService(
            UtilitiesManager utilitiesManager,
            ValidationUtils validationUtils,
            FilterService jwtService,
            ProductMappers mappers,
            SaveAndUpdateRecord saveAndUpdateRecord,
            RedisProductCacheRepo redisCacheRepo,
            ProductsRepo productsRepo
    )
    {
        this.utilitiesManager = utilitiesManager;
        this.validationUtils = validationUtils;
        this.jwtService = jwtService;
        this.mappers = mappers;
        this.saveAndUpdateRecord = saveAndUpdateRecord;
        this.redisCacheRepo = redisCacheRepo;
        this.productsRepo = productsRepo;
    }

    public ResponseEntity<AllProductResModel> getAllProduct(String token)
    {

        String jwtToken = jwtService.extractTokenFromHeader(token);
        String roles = jwtService.extractRole(jwtToken);
        String plainUUID = jwtService.extractUUID(jwtToken);
        UUID organisationId =  utilitiesManager.convertStringToUUID(jwtService.extractUUID(jwtToken)); //is a uuid
        //UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken)); //is a uuid

        validateRequest(token, plainUUID, roles);

        FetchAllProductsResModel getCacheRecord = redisCacheRepo.getAllProducts(organisationId.toString());

        if(getCacheRecord.getStatus()){
            return new ResponseEntity<>(new AllProductResModel(
                    true,
                    "message",
                    mappers.toCacheResponse(getCacheRecord.getProducts())
            ), HttpStatus.OK);

        }else{
            List<ProductDbModel> getProductRecord = fetchProduct(organisationId);
            //note: we may likely not save into cache on pull from db. since the grpc and kafka is use to manage the creating of product

            redisCacheRepo.saveAllProducts(mappers.toCacheFromProduct(getProductRecord), organisationId.toString());

            return new ResponseEntity<>(new AllProductResModel(
                    true,
                    "message",
                    mappers.toCacheResponse(mappers.toCacheFromProduct(getProductRecord))
            ), HttpStatus.OK);

        }
    }

    private List<ProductDbModel> fetchProduct(UUID organisationId) {
        return productsRepo.findByOrganisationIdAndIsActive(organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, AppConfig.GET_ALL_PRODUCT_ERROR_TAG, AppConfig.FETCH_ALL_PRODUCT_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    private void validateRequest(String token,  String uuid, String role) {
//        validationUtils.userRoleValidateRequest(role);
        validationUtils.jwtValidateRequest(token);
//        validationUtils.roleValidation(role);
        validationUtils.bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

}
