package com.dart.product.service.product_policies_warranty.create;

import com.dart.product.dependency.di.ServicesDi;
import com.dart.product.dto_model.product_policy_model.CreateProductPolicyReqDTO;
import com.dart.product.dto_model.product_policy_model.ProductPolicyResDTO;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductPolicyRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class CreateProductPolicyService {

    private static final Logger logger = LoggerFactory.getLogger(CreateProductPolicyService.class);
    private ProductPolicyRepo productPolicyRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    private CreateProductPolicyService(ProductPolicyRepo productPolicyRepo, ServicesDi di){
        this.productPolicyRepo = productPolicyRepo;
        this.jwtService = di.jwtService();
        this.utilitiesManager = di.utilitiesManager();
        this.productMappers = di.productMappers();
        this.redisProductCacheRepo = di.redisProductCacheRepo();
        this.validationUtils = di.validationUtils();
    }

    public ResponseEntity<ProductPolicyResDTO> addProductPolicy(String authToken, CreateProductPolicyReqDTO reqBody, Integer productId) {

        validateRequestToken(authToken);
        validProductId(productId);
        validateRequestBody(reqBody);

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validationUserRole(roles);
        validateBruteForceProtection(userId.toString());

//        checkIfProductPolicyExist(productId, organisationId);


//        validateRequestToken(token);
//        validateRequestBody(reqBody);
//
//        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
//        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
//        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
//        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(plainUUID);
//
//        validationUserRole(roles);
//        validateBruteForceProtection(plainUUID);
//
//        //allow only one product policies to be created for a productId once. it can be updated, deleted and create another one.
//        findByIsActiveAndProductIdAndOrganisationId(organisationId, reqBody.getProduct_id());
//
//        reqBody.setOrganisation_id(organisationId);
//        reqBody.setCreated_at(LocalDateTime.now());
//        reqBody.setUpdated_at(LocalDateTime.now());
//        reqBody.set_active(true);
//        reqBody.setId(0);
//        SaveAndUpdateProductPolicyResponse saveRecordInDb = serviceLocator
//                .getSaveAndUpdateRecord()
//                .saveProductPolicy(serviceLocator.getProductMappers().mapAddProductPolicyReqModelToDbModel(reqBody));
//
//        isRecordSaveInTheDb(saveRecordInDb);
//
//        boolean cacheRecordInMemory = serviceLocator.getRedisProductCacheRepo()
//                .saveUpdateProductPolicy(serviceLocator.getProductMappers().mapProductPolicyToCache(saveRecordInDb.getProductPolicy()));
//
//        isRecordSaveInTheCache(cacheRecordInMemory);
//
//        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
//
//        return new ResponseEntity<>(serviceLocator.getProductMappers().productPolicyOneResponseBuilder(saveRecordInDb.getProductPolicy(), "product policy successfully created"), HttpStatus.CREATED);
//
//

        return null;
    }

    private void validProductId(Integer productId) {
        validationUtils.validProductId(productId);
    }

    private void validateRequestBody(CreateProductPolicyReqDTO reqBody) {
        validationUtils.productPolicyValidateRequest(reqBody);
    }

    private void validateRequestToken(String token) {
        validationUtils.jwtValidateRequest(token);
    }

    private void validationUserRole(String role) {
        validationUtils.userRoleValidation(role);
    }

    private void validateBruteForceProtection(String userId) {
        validationUtils.bruteForceProtection(AppConfig.CREATE_PRODUCT_COMMENT_BRUTE_FORCE_PROTECTION + userId);
    }


//    private void isRecordSaveInTheCache(boolean isRecord) {
//        if(!isRecord){
//            //send through kafka
//        }
//    }
//
//    private void isRecordSaveInTheDb(SaveAndUpdateProductPolicyResponse isSave) {
//        if(!isSave.getStatus()) {
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//    }
//
//    private void validateRequestBody(CreateProductPolicyReqDTO reqBody) {
//        //do the validation
//        //serviceLocator.getValidationUtils().productSpecValidate(reqBody);
//    }
//
//    private void validateRequestToken(String token) {
//        serviceLocator.getValidationUtils().jwtValidateRequest(token);
//    }
//
//    private void validationUserRole(String role){
//        serviceLocator.getValidationUtils().roleValidation(role);
//    }
//
//    private void validateBruteForceProtection(String uuid) {
//        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
//    }
//
//    /**
//     *
//     * @param organisationId
//     * @param ProductId
//     */
//    private void findByIsActiveAndProductIdAndOrganisationId(UUID organisationId, Integer ProductId) {
//        if(serviceLocator.getProductPolicyRepo().findByIsActiveAndProductIdAndOrganisationId(true, ProductId, organisationId).isPresent()){
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), "error"),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//    }



//    private void checkIfProductPolicyExist(Integer productId, UUID organisationId) {
//        boolean isPolicyInCache = cacheService.exists("policy-" + productId + "-" + organisationId);
//        if (isPolicyInCache) {
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), "A policy already exists for this product and organisation (Cache)."),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//
//        Optional<ProductPolicyDbEntity> isProductPolicyExisted = productPolicyRepo.findByIsActiveAndProductIdAndOrganisationId(true, productId, organisationId);
//        if (isProductPolicyExisted.isPresent()) {
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), "A policy already exists for this product and organisation (Database)."),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//
//    }

}
