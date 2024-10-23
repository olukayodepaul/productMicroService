package com.dart.product.service.product;


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
public class DeleteProductService {

    private final UtilitiesManager utilitiesManager;
    private final ValidationUtils validationUtils;
    private final FilterService jwtService;
    private final ProductMappers mappers;
    private final SaveAndUpdateRecord saveAndUpdateRecord;
    private RedisProductCacheRepo redisCacheRepo;
    private ProductsRepo productsRepo;

    public DeleteProductService(
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

    public ResponseEntity<ResponseHandler> deleteProduct(String token, Integer id)
    {
        String jwtToken = jwtService.extractTokenFromHeader(token);
        String roles = jwtService.extractRole(jwtToken);
        String plainUUID = jwtService.extractUUID(jwtToken);
        UUID organisationId =  utilitiesManager.convertStringToUUID(jwtService.extractUUID(jwtToken)); //is a uuid
        //UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken)); //is a uuid

        validateRequest(token, plainUUID, roles);

        ProductDbModel isProductExisting = isProductExisting(id, organisationId); //not available, throw error

        //check if product is deleted
        if(!isProductExisting.getIsActive()){
            throw new CustomRuntimeException(
                    new ErrorHandler(false,AppConfig.DELETE_PRODUCT_ERROR_TAG , AppConfig.DELETE_PRODUCT_RESPONSE),
                    HttpStatus.BAD_REQUEST
            );
        }

        isProductExisting.setUpdated_at(LocalDateTime.now());
        isProductExisting.setIsActive(false);
        SaveAndUpdateResponse updatedResult = saveAndUpdateRecord.updateProductRecord(isProductExisting);

        if(!updatedResult.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, updatedResult.getError(), updatedResult.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }

        boolean deleteCacheResult = redisCacheRepo.deleteProduct(updatedResult.getProduct().getOrganisationId().toString(), updatedResult.getProduct().getId());
        if(!deleteCacheResult){
            //send to redis........
        }

        //todo: send updated product to searchMicroService (grpc)
        //todo: if grpc failed, then reroute through kafka

        return new ResponseEntity<>(new ResponseHandler(true, AppConfig.DELETE_PRODUCT_SUCCESS_RESPONSE), HttpStatus.OK);

    }

    private void validateRequest(String token,  String uuid, String role) {
//        validationUtils.userRoleValidateRequest(role);
        validationUtils.jwtValidateRequest(token);
//        validationUtils.roleValidation(role);
        validationUtils.bruteForceProtection(AppConfig.DELETE_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }
//productsRepo.findById(id, organisationId, true)
    private ProductDbModel isProductExisting(Integer id, UUID organisationId) {
        return productsRepo.findByIdAndOrganisationIdAndIsActive(id, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, AppConfig.UPDATE_PRODUCT_ERROR_TAG, AppConfig.UPDATE_PRODUCT_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}
