package com.dart.product.repository;

import com.dart.product.entity.product_media_model.FetchAllProductMediaModel;
import com.dart.product.entity.product_media_model.FetchIndividualMediaProductModel;
import com.dart.product.entity.product_media_model.ProductMediaCacheModel;
import com.dart.product.entity.product_model.FetchAllProductsResModel;
import com.dart.product.entity.product_model.FetchProductsResModel;
import com.dart.product.entity.product_model.ProductCacheModel;
import com.dart.product.security.FilterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class RedisProductCacheRepo {


    private static final Logger logger = LoggerFactory.getLogger(RedisProductCacheRepo.class);
    private final RedisTemplate<String, Object> redisTemplate;
    private final FilterService filterService;
    private final ObjectMapper objectMapper;

    private static final String PRODUCT_KEY = "product";
    private static final String PRODUCT_MEDIA_KEY = "product_media";


    //Response
    private static final boolean SAVE_UPDATE_SUCCESS = true;
    private static final boolean SAVE_UPDATE_FAILED = false;

    public RedisProductCacheRepo(RedisTemplate<String, Object> redisTemplate, FilterService filterService, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.filterService = filterService;
        this.objectMapper = objectMapper;
    }

    //this is being update by the kafka service to blacklist a token
    public void saveJWTBlackListedToken(String uuid, String token) {
        String subKey = "product_jwt_black_service/"+uuid;
        redisTemplate.opsForList().leftPush(subKey, token);
    }

    //the filter is using token blacklisted. no need to implement it within your application
    public boolean isTokenBlacklisted(String token) {
        try {
            String subKey = "product_jwt_black_service/" + filterService.extractUUID(token);
            List<Object> tokens = redisTemplate.opsForList().range(subKey, 0, -1);
            System.out.println(tokens);
            List<String> tokenList = tokens.stream().map(Object::toString).collect(Collectors.toList());

            if(tokenList.contains(token)) {
                return false; //mean it is available and reject login
            } else {
                return true;
            }
        }catch (Exception e) {
            logger.error("RedisCacheRepo::isTokenBlacklisted  {}", e.getMessage());
            return false;
        }
    }

    public Boolean saveUpdateProduct(ProductCacheModel product) {
        try {
            // Sub-key for identifying the user by their email
            String subKey = product.getId().toString();

            // Save or update user details in Redis hash
            redisTemplate.opsForHash().put(PRODUCT_KEY+"_"+product.getOrganisation_id(), subKey, product);

            // Return success
            return SAVE_UPDATE_SUCCESS;
        } catch (Exception e) {
            // Log the error and return failure response
            logger.error("RedisCacheRepo::saveUpdateProfile  {}", e.getMessage());
            return SAVE_UPDATE_FAILED;
        }
    }

    public boolean deleteProduct(String organisationId, Integer id) {
        try {

            Long result = redisTemplate.opsForHash().delete(PRODUCT_KEY+"_"+organisationId, id);

            return result > 0;
        } catch (Exception e) {
            logger.error("RedisCacheRepo::deleteProfile - Error occurred while saving/updating user with email {}: {}", organisationId, e.getMessage());
            return false;
        }
    }


    public FetchAllProductsResModel getAllProducts(String organisationId) {
        try {
            String key = PRODUCT_KEY+"_"+organisationId;
            Map<Object, Object> productMap = redisTemplate.opsForHash().entries(key);

            if (!productMap.isEmpty()) {
                List<ProductCacheModel> products = productMap.values().stream()
                        .map(value -> objectMapper.convertValue(value, ProductCacheModel.class))
                        .collect(Collectors.toList());
                return new FetchAllProductsResModel(true, "Addresses fetched successfully", products);
            }

            return new FetchAllProductsResModel(false, "No addresses found", Collections.emptyList());

        } catch (Exception e) {
            logger.error("Error fetching addresses for uuid {}: {}", organisationId, e.getMessage());
            return new FetchAllProductsResModel(false, e.getMessage(), Collections.emptyList());
        }
    }

    public FetchProductsResModel getProducts(String organisationId, Integer id) {
        try {

            Object cachedObject = redisTemplate.opsForHash().get(PRODUCT_KEY+"_"+organisationId, id.toString());

            if (cachedObject == null) {
                return new FetchProductsResModel(false,  "No user found in redis", null);
            }
            ProductCacheModel cacheModel = objectMapper.convertValue(cachedObject, ProductCacheModel.class);
            return new FetchProductsResModel(true, "", cacheModel);

        } catch (Exception e) {
            logger.error("RedisCacheService::fetchUserDetails - Error occurred while trying to fetch user details ID {}: {}", "", e.getMessage());
            return new FetchProductsResModel(false, e.getMessage(), new ProductCacheModel());
        }
    }

    /**
     *
     * @param productModels
     * @param organisationId
     * @return
     */
    public Boolean saveAllProducts(List<ProductCacheModel> productModels,String organisationId) {
        try {
            for (ProductCacheModel products : productModels) {
                String subKey = String.valueOf(products.getId());
                redisTemplate.opsForHash().put(PRODUCT_KEY+"_"+organisationId, subKey, products);
            }
            return SAVE_UPDATE_SUCCESS;
        }catch (Exception e){
            logger.error("RedisCacheRepo::saveAllProducts - Error occurred while saving/updating user with email {}: {}",organisationId, e.getMessage());
            return SAVE_UPDATE_FAILED;
        }
    }

    //The start of product media catch
    public Boolean saveUpdateProductMedia(ProductMediaCacheModel productMedia) {
        try {
            // Sub-key for identifying the user by their email
            String subKey = productMedia.getId().toString();
            String primaryKey =  PRODUCT_MEDIA_KEY +"_"+ productMedia.getOrganisation_id() +"_"+ productMedia.getProduct_id();
            // Save or update user details in Redis hash
            redisTemplate.opsForHash().put(primaryKey, subKey, productMedia);

            // Return success
            return SAVE_UPDATE_SUCCESS;
        } catch (Exception e) {
            // Log the error and return failure response
            logger.error("RedisCacheRepo::saveUpdateProductMedia  {}", e.getMessage());
            return SAVE_UPDATE_FAILED;
        }
    }


    public Boolean saveAllProductMedia(List<ProductMediaCacheModel> productMedia) {
        try {
            for(ProductMediaCacheModel mediaProduct: productMedia) {
                String subKey = mediaProduct.getId().toString();  // Unique sub-key for each media product
                String primaryKey = PRODUCT_MEDIA_KEY + "_" + mediaProduct.getOrganisation_id() + "_" + mediaProduct.getProduct_id();
                // Save each ProductMediaCacheModel individually
                redisTemplate.opsForHash().put(primaryKey, subKey, mediaProduct);
            }
            // Return success
            return SAVE_UPDATE_SUCCESS;
        } catch (Exception e) {
            // Log the error and return failure response
            logger.error("RedisCacheRepo::saveAllProductMedia  {}", e.getMessage());
            return SAVE_UPDATE_FAILED;
        }
    }


    public FetchAllProductMediaModel findAllProductMedia(String organisationId, String productId) {
        try {
            String key = PRODUCT_MEDIA_KEY + "_" + organisationId + "_" + productId;
            Map<Object, Object> productMediaMap = redisTemplate.opsForHash().entries(key);

            System.out.println(productMediaMap);

            if (!productMediaMap.isEmpty()) {
                List<ProductMediaCacheModel> productMedia = productMediaMap.values().stream()
                        .map(value -> objectMapper.convertValue(value, ProductMediaCacheModel.class))  // Convert each value to ProductMediaCacheModel
                        .collect(Collectors.toList());
                return new FetchAllProductMediaModel(true, "Media fetched successfully", productMedia);
            }
            return new FetchAllProductMediaModel(false, "No media found", Collections.emptyList());

        } catch (Exception e) {
            logger.error("Error fetching media for getAllProductMedia {}: {}", organisationId, e.getMessage());
            return new FetchAllProductMediaModel(false, e.getMessage(), Collections.emptyList());
        }
    }

    public FetchIndividualMediaProductModel findOneProductMedia(String organisationId, Integer productId, Integer mediaId) {
        try {

            String subKey = mediaId.toString();
            String primaryKey =  PRODUCT_MEDIA_KEY +"_"+ organisationId +"_"+ productId;
            Object cachedObject = redisTemplate.opsForHash().get(primaryKey, subKey);

            if (cachedObject == null) {
                return new FetchIndividualMediaProductModel(false,  "No user found in redis", null);
            }
            ProductMediaCacheModel cacheModel = objectMapper.convertValue(cachedObject, ProductMediaCacheModel.class);
            return new FetchIndividualMediaProductModel(true, "", cacheModel);

        } catch (Exception e) {
            logger.error("RedisCacheService::findOneProductMedia - Error occurred while trying to fetch user details ID {}: {}", "", e.getMessage());
            return new FetchIndividualMediaProductModel(false, e.getMessage(), new ProductMediaCacheModel());
        }
    }

}
