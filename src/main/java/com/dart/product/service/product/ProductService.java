package com.dart.product.service.product;


import com.dart.product.entity.product_model.ProductReqModel;
import com.dart.product.entity.product_model.ProductResModel;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final UtilitiesManager utilitiesManager;
    private final ValidationUtils validationUtils;
    private final FilterService jwtService;
    private final ProductMappers mappers;
    private final SaveAndUpdateRecord saveAndUpdateRecord;
    private RedisProductCacheRepo redisCacheRepo;

    public ProductService(
            UtilitiesManager utilitiesManager,
            ValidationUtils validationUtils,
            FilterService jwtService,
            ProductMappers mappers,
            SaveAndUpdateRecord saveAndUpdateRecord,
            RedisProductCacheRepo redisCacheRepo
    )
    {
        this.utilitiesManager = utilitiesManager;
        this.validationUtils = validationUtils;
        this.jwtService = jwtService;
        this.mappers = mappers;
        this.saveAndUpdateRecord = saveAndUpdateRecord;
        this.redisCacheRepo = redisCacheRepo;
    }

    public ResponseEntity<ProductResModel> createProduct(ProductReqModel reqModel, String token) {

        String jwtToken = jwtService.extractTokenFromHeader(token);
        String roles = jwtService.extractRole(jwtToken);
        String plainUUID = jwtService.extractUUID(jwtToken);
        UUID organisationId =  utilitiesManager.convertStringToUUID(jwtService.extractUUID(jwtToken)); //is a uuid
        //UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken)); //is a uuid

        validateRequest(jwtToken, reqModel, plainUUID, roles);

        reqModel.setOrganisation_id(organisationId);
        SaveAndUpdateResponse saveResult = saveAndUpdateRecord.saveProductRecord(reqModel);

        if(!saveResult.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, saveResult.getError(), saveResult.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }

        boolean cacheResult = redisCacheRepo.saveUpdateProduct(mappers.toProductCache(saveResult.getProduct()));
        if(!cacheResult){
            //send to redis........
        }

        //todo: send newly created product to searchMicroService (grpc)

        return new ResponseEntity<>(new ProductResModel(
                true,
                "message",
                mappers.toProductResponse(saveResult.getProduct())
        ), HttpStatus.OK);
    }

    private void validateRequest(String token, ProductReqModel requestBody, String uuid, String role) {
//        validationUtils.userRoleValidateRequest(role);
        validationUtils.jwtValidateRequest(token);
//        validationUtils.roleValidation(role);
        validationUtils.productValidateRequest(requestBody, 0);
        validationUtils.bruteForceProtection(AppConfig.ADD_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }
}
