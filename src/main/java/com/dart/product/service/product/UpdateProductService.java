package com.dart.product.service.product;


import com.dart.product.entity.product_model.ProductReqModel;
import com.dart.product.entity.product_model.ProductResModel;
import com.dart.product.entity.product_model.ProductDbModel;
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
public class UpdateProductService {

    private final UtilitiesManager utilitiesManager;
    private final ValidationUtils validationUtils;
    private final FilterService jwtService;
    private final ProductMappers mappers;
    private final SaveAndUpdateRecord saveAndUpdateRecord;
    private RedisProductCacheRepo redisCacheRepo;
    private ProductsRepo productsRepo;

    public UpdateProductService(
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


    public ResponseEntity<ProductResModel> updateProduct(ProductReqModel reqModel, String token, Integer id) {

        String jwtToken = jwtService.extractTokenFromHeader(token);
        String roles = jwtService.extractRole(jwtToken);
        String plainUUID = jwtService.extractUUID(jwtToken);

        UUID organisationId =  utilitiesManager.convertStringToUUID(jwtService.extractUUID(jwtToken)); //is a uuid
        //UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken)); //is a uuid
        validateRequest(token, reqModel, plainUUID, roles, id.toString());


        ProductDbModel isProductExisting = productExisting(id, organisationId);

        SaveAndUpdateResponse updatedResult = saveAndUpdateRecord.updateProductRecord(
                mappers.toProductBuilder(isProductExisting, reqModel)
        );

        //check if records is updated.
        if(!updatedResult.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, updatedResult.getError(), updatedResult.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }

        boolean cacheResult = redisCacheRepo.saveUpdateProduct(mappers.toProductCache(updatedResult.getProduct()));
        if(!cacheResult){
            //send to redis........
        }

        //todo: send updated product to searchMicroService (grpc)
        //todo: if grpc failed, then reroute through kafka

        return new ResponseEntity<>(new ProductResModel(
                true,
                "message",
                mappers.toProductResponse(updatedResult.getProduct())
        ), HttpStatus.OK);
    }

    private void validateRequest(String token, ProductReqModel requestBody, String uuid, String role, String id) {
//        validationUtils.userRoleValidateRequest(role);
        validationUtils.IdValidation(id);
        validationUtils.jwtValidateRequest(token);
//        validationUtils.roleValidation(role);
        validationUtils.productValidateRequest(requestBody, 0);
        validationUtils.bruteForceProtection(AppConfig.UPDATE_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private ProductDbModel productExisting(Integer id, UUID organisationId) {
        return productsRepo.findByIdAndOrganisationIdAndIsActive(id, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, AppConfig.UPDATE_PRODUCT_ERROR_TAG, AppConfig.UPDATE_PRODUCT_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}
