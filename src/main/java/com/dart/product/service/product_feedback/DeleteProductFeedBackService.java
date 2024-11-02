package com.dart.product.service.product_feedback;

import com.dart.product.dto_model.product_feedback.ProductFeedBackDbModel;
import com.dart.product.dto_model.product_feedback.ProductFeedBackOneResModel;
import com.dart.product.dto_model.product_feedback.SaveAndUpdateProductFeedBackResponse;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductFeedBackRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeleteProductFeedBackService {

//    private final ProductFeedBackRepo productFeedBackRepo;
//    private final FilterService jwtService;
//    private final UtilitiesManager utilitiesManager;
//    private final ProductMappers productMappers;
//    private final RedisProductCacheRepo redisProductCacheRepo;
//    private final ValidationUtils validationUtils;
//
//    public DeleteProductFeedBackService(
//            ProductFeedBackRepo productFeedBackRepo,
//            FilterService jwtService,
//            UtilitiesManager utilitiesManager,
//            ProductMappers productMappers,
//            RedisProductCacheRepo redisProductCacheRepo,
//            ValidationUtils validationUtils
//    ) {
//        this.productFeedBackRepo = productFeedBackRepo;
//        this.jwtService = jwtService;
//        this.utilitiesManager = utilitiesManager;
//        this.productMappers = productMappers;
//        this.redisProductCacheRepo = redisProductCacheRepo;
//        this.validationUtils = validationUtils;
//    }
//
//    public ResponseEntity<ProductFeedBackOneResModel> addProductFeedBack(
//            String token, Integer productId, Integer id) {
//
//        validateRequestToken(token);
//
//        String jwtToken = jwtService.extractTokenFromHeader(token);
//        String roles = jwtService.extractRole(jwtToken);
//        String plainUUID = jwtService.extractUUID(jwtToken);
//        UUID organisationId = utilitiesManager.convertStringToUUID(plainUUID);
//
//        validationUserRole(roles);
//        validateBruteForceProtection(plainUUID);
//
//        ProductFeedBackDbModel isProductFeedBackExistingInDb = findByIdAndOrganisationIdAndIsActive(id,  productId, organisationId);
//
//
//        isProductFeedBackExistingInDb.setUpdatedAt(LocalDateTime.now());
//        isProductFeedBackExistingInDb.setActive(false);
//        SaveAndUpdateProductFeedBackResponse saveRecordInDb = saveProductFeedBack(isProductFeedBackExistingInDb);
//
//        isRecordSaveInTheDb(saveRecordInDb);
//        boolean cacheRecordInMemory = redisProductCacheRepo.deleteProductFeedBack(productMappers.mapProductFeedBackDbModelToDbModel(saveRecordInDb.getProductFeedback()));
//
//        isRecordSaveInTheCache(cacheRecordInMemory);
//
//        //todo: send newly created product policies to searchMicroService through (grpc) if fail then, kafka using same proto buffer
//        return new ResponseEntity<>(productMappers.productFeedBackResponseBuilder(saveRecordInDb.getProductFeedback(), "product comment successfully deleted"), HttpStatus.OK);
//
//    }
//
//    private void isRecordSaveInTheCache(boolean isRecord) {
//        if(!isRecord){
//            //send through kafka
//        }
//    }
//
//    private void validateRequestToken(String token) {
//        validationUtils.accessTokenValidation(token);
//    }
//
//    private void validationUserRole(String role){
//        validationUtils.customerRoleValidation(role);
//    }
//
//    private void validateBruteForceProtection(String uuid) {
//        validationUtils.bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
//    }
//
//    private void isRecordSaveInTheDb(SaveAndUpdateProductFeedBackResponse isSave) {
//        if(!isSave.getStatus()) {
//            throw new CustomRuntimeException(
//                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), isSave.getError()),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//    }
//
//    private ProductFeedBackDbModel findByIdAndOrganisationIdAndIsActive(Integer id, Integer productId, UUID organisationId) {
//        return productFeedBackRepo.findByIdAndProductIdAndOrganisationIdAndIsActive(id,  productId, organisationId, true)
//                .orElseThrow(() -> new CustomRuntimeException(
//                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
//                        HttpStatus.NOT_FOUND
//                ));
//    }
//
//    public SaveAndUpdateProductFeedBackResponse saveProductFeedBack(ProductFeedBackDbModel regDetails) {
//        try {
//            return new SaveAndUpdateProductFeedBackResponse(true, "", productFeedBackRepo.save(regDetails)) ;
//        } catch (Exception e) {
//            return new SaveAndUpdateProductFeedBackResponse(false, e.getMessage(), ProductFeedBackDbModel.builder().build());
//        }
//    }
//

}
