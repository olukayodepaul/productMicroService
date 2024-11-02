package com.dart.product.service.product_feedback;

import com.dart.product.dto_model.product_feedback.*;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductFeedBackRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetAllProductFeedBackService {

//    private final ProductFeedBackRepo productFeedBackRepo;
//    private final FilterService jwtService;
//    private final UtilitiesManager utilitiesManager;
//    private final ProductMappers productMappers;
//    private final RedisProductCacheRepo redisProductCacheRepo;
//    private final ValidationUtils validationUtils;
//
//    public GetAllProductFeedBackService(
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
//    public ResponseEntity<ProductFeedBackAllResModel> addProductFeedBack(
//            String token, Integer productId, Integer id) {
//
//        validateRequestToken(token);
//
//
//        String jwtToken = jwtService.extractTokenFromHeader(token);
//        String roles = jwtService.extractRole(jwtToken);
//        String plainUUID = jwtService.extractUUID(jwtToken);
//        UUID organisationId = utilitiesManager.convertStringToUUID(plainUUID);
//
//        validationUserRole(roles);
//        validateBruteForceProtection(plainUUID);
//
//        FetchAllProductFeedBackModel fetchAllProductFeedBackFromCache = redisProductCacheRepo.findAllProductFeedBack(organisationId.toString(), productId);
//
//        if(fetchAllProductFeedBackFromCache.getStatus()){
//            List<ProductFeedBackDbModel> allFeedBack = productMappers.mapToAllProductFeedBack(fetchAllProductFeedBackFromCache.getProductFeedBack());
//            return new ResponseEntity<>(productMappers.allProductFeedBackResponseBuilder(allFeedBack, "product comment successfully fetch"), HttpStatus.OK);
//        }
//
//        List<ProductFeedBackDbModel> allFeedBack  = findByIdAndOrganisationIdAndIsActive(productId, organisationId);
//        return new ResponseEntity<>(productMappers.allProductFeedBackResponseBuilder(allFeedBack, "product comment successfully fetch"), HttpStatus.OK);
//
//    }
//
//    private void validateRequestToken(String token) {
//        validationUtils.jwtValidateRequest(token);
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
//    private List<ProductFeedBackDbModel> findByIdAndOrganisationIdAndIsActive(Integer productId, UUID organisationId) {
//        return productFeedBackRepo.findByProductIdAndOrganisationIdAndIsActive( productId, organisationId, true)
//                .orElseThrow(() -> new CustomRuntimeException(
//                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.DELETED_MEDIA_ERROR_RESPONSE),
//                        HttpStatus.NOT_FOUND
//                ));
//    }
//

}
