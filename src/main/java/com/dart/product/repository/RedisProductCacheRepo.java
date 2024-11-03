package com.dart.product.repository;

import com.dart.product.dto_model.product_comments_model.FetchAllProductCommentModel;
import com.dart.product.dto_model.product_comments_model.FetchProductCommentModel;
import com.dart.product.entity.product_comment_entity.ProductCommentCacheEntity;
import com.dart.product.dto_model.product_feedback.FetchAllProductFeedBackModel;
import com.dart.product.dto_model.product_feedback.FetchProductFeedBackModel;
import com.dart.product.entity.product_feedback_entity.ProductFeedBackCacheEntity;
import com.dart.product.dto_model.product_media_model.FetchAllProductMediaModel;
import com.dart.product.dto_model.product_media_model.FetchOneProductMediaModel;
import com.dart.product.dto_model.product_media_model.ProductMediaCacheModel;
import com.dart.product.dto_model.product_dto_model.FetchAllProductsResModel;
import com.dart.product.dto_model.product_dto_model.FetchProductsResModel;
import com.dart.product.entity.product_entity.ProductCacheEntity;
import com.dart.product.dto_model.product_policy_model.FetchAllProductPolicyModel;
import com.dart.product.dto_model.product_policy_model.FetchOnelProductPolicyModel;
import com.dart.product.dto_model.product_policy_model.ProductPolicyCacheModel;
import com.dart.product.dto_model.product_reviews_model.FetchAllProductReviewModel;
import com.dart.product.dto_model.product_reviews_model.FetchOneProductReviewModel;
import com.dart.product.dto_model.product_reviews_model.ProductReviewCacheModel;
import com.dart.product.dto_model.product_specification_model.FetchAllProductSpecModel;
import com.dart.product.dto_model.product_specification_model.FetchOnelProductSpecModel;
import com.dart.product.dto_model.product_specification_model.ProductSpecificationCacheModel;
import com.dart.product.dto_model.product_tags_model.FetchAllProductTagModel;
import com.dart.product.dto_model.product_tags_model.FetchOneProductTagModel;
import com.dart.product.dto_model.product_tags_model.ProductTagCacheModel;
import com.dart.product.dto_model.related_products_model.FetchAllRelatedProductModel;
import com.dart.product.dto_model.related_products_model.FetchOneRelatedProductsModel;
import com.dart.product.dto_model.related_products_model.RelatedProductsCacheModel;
import com.dart.product.dto_model.shipping_details_model.FetchAllShippingDetailsModel;
import com.dart.product.dto_model.shipping_details_model.FetchOnelShippingDetailsModel;
import com.dart.product.dto_model.shipping_details_model.ShippingDetailsCacheModel;
import com.dart.product.dto_model.special_offers_model.FetchAllSpecialOfferModel;
import com.dart.product.dto_model.special_offers_model.FetchOneSpecialOfferModel;
import com.dart.product.dto_model.special_offers_model.SpecialOffersCacheModel;
import com.dart.product.security.FilterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
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
    private static final String PRODUCT_SPECIFICATION_KEY = "product_specification";
    private static final String SHIPPING_DETAILS_KEY = "shipping_details";
    private static final String PRODUCT_POLICY_KEY = "product_policy";
    private static final String PRODUCT_REVIEW_KEY = "product_review";
    private static final String RELATED_PRODUCT_KEY = "related_product";
    private static final String SPECIAL_OFFER_KEY  = "special_offer";
    private static final String PRODUCT_TAG_KEY  = "product_tag";
    private static final String PRODUCT_COMMENT_KEY  = "product_comment";
    private static final String PRODUCT_FEEDBACK_KEY  = "product_feedback";


    //Response
    private static final boolean SAVE_UPDATE_SUCCESS = true;
    private static final boolean SAVE_UPDATE_FAILED = false;

    public RedisProductCacheRepo(RedisTemplate<String, Object> redisTemplate, FilterService filterService, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.filterService = filterService;
        this.objectMapper = objectMapper;
    }

    //this is being update by the kafka service to blacklist a token
    public void saveJWTBlackListedToken(String userId, String token) {
        String subKey = "product_jwt_black_service/"+userId;
        redisTemplate.opsForList().leftPush(subKey, token);
    }

    //the filter is using token blacklisted. no need to implement it within your application
    public boolean isTokenBlacklisted(String token) {
        try {
            String subKey = "product_jwt_black_service/" + filterService.extractUserId(token);
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

    //product
    public Boolean saveUpdateProduct(ProductCacheEntity product) {
        try {
            // Sub-key for identifying the user by their email
            String subKey = product.getId().toString();
            String primaryKey =  PRODUCT_KEY +"_"+ product.getOrganisation_id();
            // Save or update user details in Redis hash
            redisTemplate.opsForHash().put(primaryKey, subKey, product);

            // Return success
            return SAVE_UPDATE_SUCCESS;
        } catch (Exception e) {
            // Log the error and return failure response
            logger.error("RedisCacheRepo::saveUpdateProduct  {}", e.getMessage());
            return SAVE_UPDATE_FAILED;
        }
    }

    public boolean deleteProduct(String organisationId, Integer id) {
        try {

            String subKey = id.toString();
            String primaryKey =  PRODUCT_KEY +"_"+ organisationId;

            Long result = redisTemplate.opsForHash().delete(primaryKey, subKey);

            return result > 0;
        } catch (Exception e) {
            logger.error("RedisCacheRepo::deleteProduct {}: {}",organisationId, e.getMessage());
            return false;
        }
    }

    public FetchProductsResModel getProducts(String organisationId, Integer id) {
        try {

            String subKey = id.toString();
            String primaryKey =  PRODUCT_KEY +"_"+ organisationId ;
            Object cachedObject = redisTemplate.opsForHash().get(primaryKey, subKey);

            if (cachedObject == null) {
                return new FetchProductsResModel(false,  "No user found in redis", null);
            }
            ProductCacheEntity cacheModel = objectMapper.convertValue(cachedObject, ProductCacheEntity.class);
            return new FetchProductsResModel(true, "", cacheModel);

        } catch (Exception e) {
            logger.error("RedisCacheService::getProducts {}: {}", "", e.getMessage());
            return new FetchProductsResModel(false, e.getMessage(), new ProductCacheEntity());
        }
    }


    public FetchAllProductsResModel getAllProducts(String organisationId) {
        try {
            String key = PRODUCT_KEY + "_" + organisationId;
            Map<Object, Object> productMap = redisTemplate.opsForHash().entries(key);

            if (!productMap.isEmpty()) {
                List<ProductCacheEntity> products = productMap.values().stream()
                        .map(value -> objectMapper.convertValue(value, ProductCacheEntity.class))
                        .sorted(Comparator.comparing(ProductCacheEntity::getId))
                        .collect(Collectors.toList());

                return new FetchAllProductsResModel(true, "Addresses fetched successfully",  products);
            }
            return new FetchAllProductsResModel(false, "No addresses found",  Collections.emptyList());

        } catch (Exception e) {
            logger.error("Error fetching addresses for uuid {}: {}", organisationId, e.getMessage());
            return new FetchAllProductsResModel(false, e.getMessage(),  Collections.emptyList());
        }
    }


    //save record for product comment
    public Boolean saveUpdateProductComment(ProductCommentCacheEntity productComment) {
        try {
            // Sub-key for identifying the user by their email
            String subKey = productComment.getId().toString();
            String primaryKey =  PRODUCT_COMMENT_KEY +"_"+ productComment.getOrganisationId() +"_"+ productComment.getProductId();
            // Save or update user details in Redis hash
            redisTemplate.opsForHash().put(primaryKey, subKey, productComment);

            // Return success
            return SAVE_UPDATE_SUCCESS;
        } catch (Exception e) {
            // Log the error and return failure response
            logger.error("RedisCacheRepo::saveUpdateProductComment  {}", e.getMessage());
            return SAVE_UPDATE_FAILED;
        }
    }

    public boolean deleteProductComment(ProductCommentCacheEntity productComment) {
        try {

            String subKey = productComment.getId().toString();
            String primaryKey =  PRODUCT_COMMENT_KEY +"_"+ productComment.getOrganisationId() +"_"+ productComment.getProductId();

            Long result = redisTemplate.opsForHash().delete(primaryKey, subKey);

            return result > 0;
        } catch (Exception e) {
            logger.error("RedisCacheRepo::deleteProductComment {}: {}", productComment.getOrganisationId(), e.getMessage());
            return false;
        }
    }

    public FetchProductCommentModel findOneProductComment(String organisationId, Integer productId, Integer productSpecId) {
        try {

            String subKey = productSpecId.toString();
            String primaryKey =  PRODUCT_COMMENT_KEY +"_"+ organisationId +"_"+ productId;
            Object cachedObject = redisTemplate.opsForHash().get(primaryKey, subKey);

            if (cachedObject == null) {
                return new FetchProductCommentModel(false,  "No user found in redis", null);
            }
            ProductCommentCacheEntity cacheModel = objectMapper.convertValue(cachedObject, ProductCommentCacheEntity.class);
            return new FetchProductCommentModel(true, "", cacheModel);

        } catch (Exception e) {
            logger.error("RedisCacheService::findOneProductComment {}: {}", "", e.getMessage());
            return new FetchProductCommentModel(false, e.getMessage(), new ProductCommentCacheEntity());
        }
    }

    public FetchAllProductCommentModel findAllProductComment(String organisationId, Integer productId) {
        try {
            String key = PRODUCT_COMMENT_KEY + "_" + organisationId + "_" + productId;
            Map<Object, Object> productCommentMap = redisTemplate.opsForHash().entries(key);

            if (!productCommentMap.isEmpty()) {
                List<ProductCommentCacheEntity> productComment = productCommentMap.values().stream()
                        .map(value -> objectMapper.convertValue(value, ProductCommentCacheEntity.class))
                        .sorted(Comparator.comparing(ProductCommentCacheEntity::getId))
                        .collect(Collectors.toList());
                return new FetchAllProductCommentModel(true, "Product Tage fetched successfully", productComment);
            }
            return new FetchAllProductCommentModel(false, "No media found", Collections.emptyList());

        } catch (Exception e) {
            logger.error("Error fetching media for findAllProductComment {}: {}", organisationId, e.getMessage());
            return new FetchAllProductCommentModel(false, e.getMessage(), Collections.emptyList());
        }
    }

    //save record for product feedback
    public Boolean saveUpdateProductFeedBack(ProductFeedBackCacheEntity productFeedBack) {
        try {
            String subKey = productFeedBack.getProductId().toString();
            String primaryKey =  PRODUCT_FEEDBACK_KEY +"_"+ productFeedBack.getOrganisationId();
            redisTemplate.opsForHash().put(primaryKey, subKey, productFeedBack);
            return SAVE_UPDATE_SUCCESS;
        } catch (Exception e) {
            logger.error("RedisCacheRepo::saveUpdateProductFeedBack  {}", e.getMessage());
            return SAVE_UPDATE_FAILED;
        }
    }

    public FetchProductFeedBackModel findOneProductFeedBack(String organisationId, Integer productId) {
        try {

            String subKey = productId.toString();
            String primaryKey =  PRODUCT_FEEDBACK_KEY +"_"+ organisationId;
            Object cachedObject = redisTemplate.opsForHash().get(primaryKey, subKey);

            if (cachedObject == null) {
                return new FetchProductFeedBackModel(false,  "No user found in redis", null);
            }
            ProductFeedBackCacheEntity cacheModel = objectMapper.convertValue(cachedObject, ProductFeedBackCacheEntity.class);
            return new FetchProductFeedBackModel(true, "", cacheModel);

        } catch (Exception e) {
            logger.error("RedisCacheService::findOneProductFeedBack {}: {}", "", e.getMessage());
            return new FetchProductFeedBackModel(false, e.getMessage(), new ProductFeedBackCacheEntity());
        }
    }

    public FetchAllProductFeedBackModel findAllProductFeedBack(String organisationId) {
        try {
            String key = PRODUCT_FEEDBACK_KEY + "_" + organisationId;
            Map<Object, Object> productFeedBackMap = redisTemplate.opsForHash().entries(key);

            if (!productFeedBackMap.isEmpty()) {
                List<ProductFeedBackCacheEntity> productFeedBack = productFeedBackMap.values().stream()
                        .map(value -> objectMapper.convertValue(value, ProductFeedBackCacheEntity.class))
                        .sorted(Comparator.comparing(ProductFeedBackCacheEntity::getId))
                        .collect(Collectors.toList());
                return new FetchAllProductFeedBackModel(true, "Product Tage fetched successfully", productFeedBack);
            }
            return new FetchAllProductFeedBackModel(false, "No media found", Collections.emptyList());

        } catch (Exception e) {
            logger.error("Error fetching media for findAllProductFeedBack {}: {}", organisationId, e.getMessage());
            return new FetchAllProductFeedBackModel(false, e.getMessage(), Collections.emptyList());
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

    public FetchOneProductMediaModel findOneProductMedia(String organisationId, Integer productId, Integer mediaId) {
        try {

            String subKey = mediaId.toString();
            String primaryKey =  PRODUCT_MEDIA_KEY +"_"+ organisationId +"_"+ productId;
            Object cachedObject = redisTemplate.opsForHash().get(primaryKey, subKey);

            if (cachedObject == null) {
                return new FetchOneProductMediaModel(false,  "No user found in redis", null);
            }
            ProductMediaCacheModel cacheModel = objectMapper.convertValue(cachedObject, ProductMediaCacheModel.class);
            return new FetchOneProductMediaModel(true, "", cacheModel);

        } catch (Exception e) {
            logger.error("RedisCacheService::findOneProductMedia - Error occurred while trying to fetch user details ID {}: {}", "", e.getMessage());
            return new FetchOneProductMediaModel(false, e.getMessage(), new ProductMediaCacheModel());
        }
    }


    //save record for product specification
    public Boolean saveUpdateProductSpec(ProductSpecificationCacheModel productSpec) {
        try {
            // Sub-key for identifying the user by their email
            String subKey = productSpec.getId().toString();
            String primaryKey =  PRODUCT_SPECIFICATION_KEY +"_"+ productSpec.getOrganisationId() +"_"+ productSpec.getProductId();
            // Save or update user details in Redis hash
            redisTemplate.opsForHash().put(primaryKey, subKey, productSpec);

            // Return success
            return SAVE_UPDATE_SUCCESS;
        } catch (Exception e) {
            // Log the error and return failure response
            logger.error("RedisCacheRepo::saveUpdateProductSpec  {}", e.getMessage());
            return SAVE_UPDATE_FAILED;
        }
    }

    public boolean deleteProductSpec(ProductSpecificationCacheModel productSpec) {
        try {

            String subKey = productSpec.getId().toString();
            String primaryKey =  PRODUCT_SPECIFICATION_KEY +"_"+ productSpec.getOrganisationId() +"_"+ productSpec.getProductId();

            Long result = redisTemplate.opsForHash().delete(primaryKey, subKey);

            return result > 0;
        } catch (Exception e) {
            logger.error("RedisCacheRepo::deleteProductSpec - Error occurred while saving/updating user with email {}: {}", productSpec.getOrganisationId(), e.getMessage());
            return false;
        }
    }

    public FetchOnelProductSpecModel findOneProductSpec(String organisationId, Integer productId, Integer productSpecId) {
        try {

            String subKey = productSpecId.toString();
            String primaryKey =  PRODUCT_SPECIFICATION_KEY +"_"+ organisationId +"_"+ productId;
            Object cachedObject = redisTemplate.opsForHash().get(primaryKey, subKey);

            if (cachedObject == null) {
                return new FetchOnelProductSpecModel(false,  "No user found in redis", null);
            }
            ProductSpecificationCacheModel cacheModel = objectMapper.convertValue(cachedObject, ProductSpecificationCacheModel.class);
            return new FetchOnelProductSpecModel(true, "", cacheModel);

        } catch (Exception e) {
            logger.error("RedisCacheService::findOneProductSpec - Error occurred while trying to fetch user details ID {}: {}", "", e.getMessage());
            return new FetchOnelProductSpecModel(false, e.getMessage(), new ProductSpecificationCacheModel());
        }
    }

    public FetchAllProductSpecModel findAllProductSpec(String organisationId, Integer productId) {
        try {
            String key = PRODUCT_SPECIFICATION_KEY + "_" + organisationId + "_" + productId;
            Map<Object, Object> productSpecMap = redisTemplate.opsForHash().entries(key);

            System.out.println(productSpecMap);

            if (!productSpecMap.isEmpty()) {
                List<ProductSpecificationCacheModel> productSpec = productSpecMap.values().stream()
                        .map(value -> objectMapper.convertValue(value, ProductSpecificationCacheModel.class))
                        .collect(Collectors.toList());
                return new FetchAllProductSpecModel(true, "Product Specification fetched successfully", productSpec);
            }
            return new FetchAllProductSpecModel(false, "No media found", Collections.emptyList());

        } catch (Exception e) {
            logger.error("Error fetching media for findAllProductSpec {}: {}", organisationId, e.getMessage());
            return new FetchAllProductSpecModel(false, e.getMessage(), Collections.emptyList());
        }
    }

    //shipping Details
    public Boolean saveUpdateShippingDetails(ShippingDetailsCacheModel shippingDetails) {
        try {
            // Sub-key for identifying the user by their email
            String subKey = shippingDetails.getId().toString();
            String primaryKey =  SHIPPING_DETAILS_KEY +"_"+ shippingDetails.getOrganisationId() +"_"+ shippingDetails.getProductId();
            // Save or update user details in Redis hash
            redisTemplate.opsForHash().put(primaryKey, subKey, shippingDetails);

            // Return success
            return SAVE_UPDATE_SUCCESS;
        } catch (Exception e) {
            // Log the error and return failure response
            logger.error("RedisCacheRepo::saveUpdateShippingDetails  {}", e.getMessage());
            return SAVE_UPDATE_FAILED;
        }
    }

    public boolean deleteShippingDetails(ShippingDetailsCacheModel shippingDetails) {
        try {

            String subKey = shippingDetails.getId().toString();
            String primaryKey =  SHIPPING_DETAILS_KEY +"_"+ shippingDetails.getOrganisationId() +"_"+ shippingDetails.getProductId();

            Long result = redisTemplate.opsForHash().delete(primaryKey, subKey);

            return result > 0;
        } catch (Exception e) {
            logger.error("RedisCacheRepo::deleteShippingDetails - Error occurred while saving/updating user with email {}: {}", shippingDetails.getOrganisationId(), e.getMessage());
            return false;
        }
    }

    public FetchOnelShippingDetailsModel findOneShippingDetails(String organisationId, Integer productId, Integer productSpecId) {
        try {

            String subKey = productSpecId.toString();
            String primaryKey =  SHIPPING_DETAILS_KEY +"_"+ organisationId +"_"+ productId;
            Object cachedObject = redisTemplate.opsForHash().get(primaryKey, subKey);

            if (cachedObject == null) {
                return new FetchOnelShippingDetailsModel(false,  "No user found in redis", null);
            }
            ShippingDetailsCacheModel cacheModel = objectMapper.convertValue(cachedObject, ShippingDetailsCacheModel.class);
            return new FetchOnelShippingDetailsModel(true, "", cacheModel);

        } catch (Exception e) {
            logger.error("RedisCacheService::findOneShippingDetails - Error occurred while trying to fetch user details ID {}: {}", "", e.getMessage());
            return new FetchOnelShippingDetailsModel(false, e.getMessage(), new ShippingDetailsCacheModel());
        }
    }


    public FetchAllShippingDetailsModel findAllShippingDetails(String organisationId, Integer productId) {
        try {
            String key = SHIPPING_DETAILS_KEY + "_" + organisationId + "_" + productId;
            Map<Object, Object> productSpecMap = redisTemplate.opsForHash().entries(key);

            System.out.println(productSpecMap);

            if (!productSpecMap.isEmpty()) {
                List<ShippingDetailsCacheModel> productSpec = productSpecMap.values().stream()
                        .map(value -> objectMapper.convertValue(value, ShippingDetailsCacheModel.class))
                        .collect(Collectors.toList());
                return new FetchAllShippingDetailsModel(true, "Product Specification fetched successfully", productSpec);
            }
            return new FetchAllShippingDetailsModel(false, "No media found", Collections.emptyList());

        } catch (Exception e) {
            logger.error("Error fetching media for findAllShippingDetails {}: {}", organisationId, e.getMessage());
            return new FetchAllShippingDetailsModel(false, e.getMessage(), Collections.emptyList());
        }
    }

    //save record for product policies
    public Boolean saveUpdateProductPolicy(ProductPolicyCacheModel productSpec) {
        try {
            // Sub-key for identifying the user by their email
            String subKey = productSpec.getId().toString();
            String primaryKey =  PRODUCT_POLICY_KEY +"_"+ productSpec.getOrganisationId() +"_"+ productSpec.getProductId();
            // Save or update user details in Redis hash
            redisTemplate.opsForHash().put(primaryKey, subKey, productSpec);

            // Return success
            return SAVE_UPDATE_SUCCESS;
        } catch (Exception e) {
            // Log the error and return failure response
            logger.error("RedisCacheRepo::saveUpdateProductPolicy  {}", e.getMessage());
            return SAVE_UPDATE_FAILED;
        }
    }

    public boolean deleteProductPolicy(ProductPolicyCacheModel productSpec) {
        try {

            String subKey = productSpec.getId().toString();
            String primaryKey =  PRODUCT_POLICY_KEY +"_"+ productSpec.getOrganisationId() +"_"+ productSpec.getProductId();

            Long result = redisTemplate.opsForHash().delete(primaryKey, subKey);

            return result > 0;
        } catch (Exception e) {
            logger.error("RedisCacheRepo::deleteProductPolicy - Error occurred while saving/updating user with email {}: {}", productSpec.getOrganisationId(), e.getMessage());
            return false;
        }
    }

    public FetchOnelProductPolicyModel findOneProductPolicy(String organisationId, Integer productId, Integer productSpecId) {
        try {

            String subKey = productSpecId.toString();
            String primaryKey =  PRODUCT_POLICY_KEY +"_"+ organisationId +"_"+ productId;
            Object cachedObject = redisTemplate.opsForHash().get(primaryKey, subKey);

            if (cachedObject == null) {
                return new FetchOnelProductPolicyModel(false,  "No user found in redis", null);
            }
            ProductPolicyCacheModel cacheModel = objectMapper.convertValue(cachedObject, ProductPolicyCacheModel.class);
            return new FetchOnelProductPolicyModel(true, "", cacheModel);

        } catch (Exception e) {
            logger.error("RedisCacheService::findOneProductPolicy - Error occurred while trying to fetch user details ID {}: {}", "", e.getMessage());
            return new FetchOnelProductPolicyModel(false, e.getMessage(), new ProductPolicyCacheModel());
        }
    }

    public FetchAllProductPolicyModel findAllProductPolicy(String organisationId, Integer productId) {
        try {
            String key = PRODUCT_POLICY_KEY + "_" + organisationId + "_" + productId;
            Map<Object, Object> productPolicyMap = redisTemplate.opsForHash().entries(key);

            if (!productPolicyMap.isEmpty()) {
                List<ProductPolicyCacheModel> productPolicy = productPolicyMap.values().stream()
                        .map(value -> objectMapper.convertValue(value, ProductPolicyCacheModel.class))
                        .collect(Collectors.toList());
                return new FetchAllProductPolicyModel(true, "Product Specification fetched successfully", productPolicy);
            }
            return new FetchAllProductPolicyModel(false, "No media found", Collections.emptyList());

        } catch (Exception e) {
            logger.error("Error fetching media for findAllProductPolicy {}: {}", organisationId, e.getMessage());
            return new FetchAllProductPolicyModel(false, e.getMessage(), Collections.emptyList());
        }
    }


    //save record for product review
    public Boolean saveUpdateProductReview(ProductReviewCacheModel productReview) {
        try {
            // Sub-key for identifying the user by their email
            String subKey = productReview.getId().toString();
            String primaryKey =  PRODUCT_REVIEW_KEY +"_"+ productReview.getOrganisationId() +"_"+ productReview.getProductId();
            // Save or update user details in Redis hash
            redisTemplate.opsForHash().put(primaryKey, subKey, productReview);

            // Return success
            return SAVE_UPDATE_SUCCESS;
        } catch (Exception e) {
            // Log the error and return failure response
            logger.error("RedisCacheRepo::saveUpdateProductReview  {}", e.getMessage());
            return SAVE_UPDATE_FAILED;
        }
    }

    public boolean deleteProductReview(ProductReviewCacheModel productReview) {
        try {

            String subKey = productReview.getId().toString();
            String primaryKey =  PRODUCT_REVIEW_KEY +"_"+ productReview.getOrganisationId() +"_"+ productReview.getProductId();

            Long result = redisTemplate.opsForHash().delete(primaryKey, subKey);

            return result > 0;
        } catch (Exception e) {
            logger.error("RedisCacheRepo::deleteProductReview  {}: {}", productReview.getOrganisationId(), e.getMessage());
            return false;
        }
    }

    public FetchOneProductReviewModel findOneProductReview(String organisationId, Integer productId, Integer productSpecId) {
        try {

            String subKey = productSpecId.toString();
            String primaryKey =  PRODUCT_REVIEW_KEY +"_"+ organisationId +"_"+ productId;
            Object cachedObject = redisTemplate.opsForHash().get(primaryKey, subKey);

            if (cachedObject == null) {
                return new FetchOneProductReviewModel(false,  "No user found in redis", null);
            }
            ProductReviewCacheModel cacheModel = objectMapper.convertValue(cachedObject, ProductReviewCacheModel.class);
            return new FetchOneProductReviewModel(true, "", cacheModel);

        } catch (Exception e) {
            logger.error("RedisCacheService::findOneProductReview {}: {}", "", e.getMessage());
            return new FetchOneProductReviewModel(false, e.getMessage(), new ProductReviewCacheModel());
        }
    }

    public FetchAllProductReviewModel findAllProductReview(String organisationId, Integer productId) {
        try {
            String key = PRODUCT_REVIEW_KEY + "_" + organisationId + "_" + productId;
            Map<Object, Object> productPolicyMap = redisTemplate.opsForHash().entries(key);

            if (!productPolicyMap.isEmpty()) {
                List<ProductReviewCacheModel> productReview = productPolicyMap.values().stream()
                        .map(value -> objectMapper.convertValue(value, ProductReviewCacheModel.class))
                        .collect(Collectors.toList());
                return new FetchAllProductReviewModel(true, "Product Review fetched successfully", productReview);
            }
            return new FetchAllProductReviewModel(false, "No media found", Collections.emptyList());

        } catch (Exception e) {
            logger.error("Error fetching media for findAllProductReview {}: {}", organisationId, e.getMessage());
            return new FetchAllProductReviewModel(false, e.getMessage(), Collections.emptyList());
        }
    }

    //save record for related product
    public Boolean saveUpdateRelatedProduct(RelatedProductsCacheModel relatedProduct) {
        try {
            // Sub-key for identifying the user by their email
            String subKey = relatedProduct.getId().toString();
            String primaryKey =  RELATED_PRODUCT_KEY +"_"+ relatedProduct.getOrganisationId() +"_"+ relatedProduct.getProductId();
            // Save or update user details in Redis hash
            redisTemplate.opsForHash().put(primaryKey, subKey, relatedProduct);

            // Return success
            return SAVE_UPDATE_SUCCESS;
        } catch (Exception e) {
            // Log the error and return failure response
            logger.error("RedisCacheRepo::saveUpdateRelatedProduct  {}", e.getMessage());
            return SAVE_UPDATE_FAILED;
        }
    }

    public boolean deleteRelatedProduct(RelatedProductsCacheModel relatedProduct) {
        try {

            String subKey = relatedProduct.getId().toString();
            String primaryKey =  RELATED_PRODUCT_KEY +"_"+ relatedProduct.getOrganisationId() +"_"+ relatedProduct.getProductId();

            Long result = redisTemplate.opsForHash().delete(primaryKey, subKey);

            return result > 0;
        } catch (Exception e) {
            logger.error("RedisCacheRepo::deleteRelatedProduct {}: {}", relatedProduct.getOrganisationId(), e.getMessage());
            return false;
        }
    }

    public FetchOneRelatedProductsModel findOneRelatedProducts(String organisationId, Integer productId, Integer productSpecId) {
        try {

            String subKey = productSpecId.toString();
            String primaryKey =  RELATED_PRODUCT_KEY +"_"+ organisationId +"_"+ productId;
            Object cachedObject = redisTemplate.opsForHash().get(primaryKey, subKey);

            if (cachedObject == null) {
                return new FetchOneRelatedProductsModel(false,  "No user found in redis", null);
            }
            RelatedProductsCacheModel cacheModel = objectMapper.convertValue(cachedObject, RelatedProductsCacheModel.class);
            return new FetchOneRelatedProductsModel(true, "", cacheModel);

        } catch (Exception e) {
            logger.error("RedisCacheService::findOneRelatedProducts {}: {}", "", e.getMessage());
            return new FetchOneRelatedProductsModel(false, e.getMessage(), new RelatedProductsCacheModel());
        }
    }

    public FetchAllRelatedProductModel findAllRelatedProduct(String organisationId, Integer productId) {
        try {
            String key = RELATED_PRODUCT_KEY + "_" + organisationId + "_" + productId;
            Map<Object, Object> ReviewProductMap = redisTemplate.opsForHash().entries(key);

            if (!ReviewProductMap.isEmpty()) {
                List<RelatedProductsCacheModel> relatedProduct = ReviewProductMap.values().stream()
                        .map(value -> objectMapper.convertValue(value, RelatedProductsCacheModel.class))
                        .collect(Collectors.toList());
                return new FetchAllRelatedProductModel(true, "Product Review fetched successfully", relatedProduct);
            }
            return new FetchAllRelatedProductModel(false, "No media found", Collections.emptyList());

        } catch (Exception e) {
            logger.error("Error fetching media for findAllRelatedProduct {}: {}", organisationId, e.getMessage());
            return new FetchAllRelatedProductModel(false, e.getMessage(), Collections.emptyList());
        }
    }

    //save record for special offer
    public Boolean saveUpdateSpecialOffers(SpecialOffersCacheModel specialOffer) {
        try {
            // Sub-key for identifying the user by their email
            String subKey = specialOffer.getId().toString();
            String primaryKey =  SPECIAL_OFFER_KEY +"_"+ specialOffer.getOrganisationId() +"_"+ specialOffer.getProductId();
            // Save or update user details in Redis hash
            redisTemplate.opsForHash().put(primaryKey, subKey, specialOffer);

            // Return success
            return SAVE_UPDATE_SUCCESS;
        } catch (Exception e) {
            // Log the error and return failure response
            logger.error("RedisCacheRepo::saveUpdateSpecialOffers  {}", e.getMessage());
            return SAVE_UPDATE_FAILED;
        }
    }

    public boolean deleteSpecialOffers(SpecialOffersCacheModel specialOffer) {
        try {

            String subKey = specialOffer.getId().toString();
            String primaryKey =  SPECIAL_OFFER_KEY +"_"+ specialOffer.getOrganisationId() +"_"+ specialOffer.getProductId();

            Long result = redisTemplate.opsForHash().delete(primaryKey, subKey);

            return result > 0;
        } catch (Exception e) {
            logger.error("RedisCacheRepo::deleteSpecialOffers {}: {}", specialOffer.getOrganisationId(), e.getMessage());
            return false;
        }
    }

    public FetchOneSpecialOfferModel findOneSpecialOffer(String organisationId, Integer productId, Integer productSpecId) {
        try {

            String subKey = productSpecId.toString();
            String primaryKey =  SPECIAL_OFFER_KEY +"_"+ organisationId +"_"+ productId;
            Object cachedObject = redisTemplate.opsForHash().get(primaryKey, subKey);

            if (cachedObject == null) {
                return new FetchOneSpecialOfferModel(false,  "No user found in redis", null);
            }
            SpecialOffersCacheModel cacheModel = objectMapper.convertValue(cachedObject, SpecialOffersCacheModel.class);
            return new FetchOneSpecialOfferModel(true, "", cacheModel);

        } catch (Exception e) {
            logger.error("RedisCacheService::findOneSpecialOffer {}: {}", "", e.getMessage());
            return new FetchOneSpecialOfferModel(false, e.getMessage(), new SpecialOffersCacheModel());
        }
    }

    public FetchAllSpecialOfferModel findAllSpecialOffer(String organisationId, Integer productId) {
        try {
            String key = SPECIAL_OFFER_KEY + "_" + organisationId + "_" + productId;
            Map<Object, Object> specialOfferMap = redisTemplate.opsForHash().entries(key);

            if (!specialOfferMap.isEmpty()) {
                List<SpecialOffersCacheModel> specialOffer = specialOfferMap.values().stream()
                        .map(value -> objectMapper.convertValue(value, SpecialOffersCacheModel.class))
                        .collect(Collectors.toList());
                return new FetchAllSpecialOfferModel(true, "Product Review fetched successfully", specialOffer);
            }
            return new FetchAllSpecialOfferModel(false, "No media found", Collections.emptyList());

        } catch (Exception e) {
            logger.error("Error fetching media for findAllSpecialOffer {}: {}", organisationId, e.getMessage());
            return new FetchAllSpecialOfferModel(false, e.getMessage(), Collections.emptyList());
        }
    }






    //save record for product tag
    public Boolean saveUpdateProductTag(ProductTagCacheModel productTag) {
        try {
            // Sub-key for identifying the user by their email
            String subKey = productTag.getId().toString();
            String primaryKey =  PRODUCT_TAG_KEY +"_"+ productTag.getOrganisationId() +"_"+ productTag.getProductId();
            // Save or update user details in Redis hash
            redisTemplate.opsForHash().put(primaryKey, subKey, productTag);

            // Return success
            return SAVE_UPDATE_SUCCESS;
        } catch (Exception e) {
            // Log the error and return failure response
            logger.error("RedisCacheRepo::saveUpdateProductTag  {}", e.getMessage());
            return SAVE_UPDATE_FAILED;
        }
    }

    public boolean deleteProductTag(ProductTagCacheModel productTag) {
        try {

            String subKey = productTag.getId().toString();
            String primaryKey =  PRODUCT_TAG_KEY +"_"+ productTag.getOrganisationId() +"_"+ productTag.getProductId();

            Long result = redisTemplate.opsForHash().delete(primaryKey, subKey);

            return result > 0;
        } catch (Exception e) {
            logger.error("RedisCacheRepo::deleteProductTag {}: {}", productTag.getOrganisationId(), e.getMessage());
            return false;
        }
    }

    public FetchOneProductTagModel findOneProductTag(String organisationId, Integer productId, Integer productSpecId) {
        try {

            String subKey = productSpecId.toString();
            String primaryKey =  PRODUCT_TAG_KEY +"_"+ organisationId +"_"+ productId;
            Object cachedObject = redisTemplate.opsForHash().get(primaryKey, subKey);

            if (cachedObject == null) {
                return new FetchOneProductTagModel(false,  "No user found in redis", null);
            }
            ProductTagCacheModel cacheModel = objectMapper.convertValue(cachedObject, ProductTagCacheModel.class);
            return new FetchOneProductTagModel(true, "", cacheModel);

        } catch (Exception e) {
            logger.error("RedisCacheService::findOneProductTag {}: {}", "", e.getMessage());
            return new FetchOneProductTagModel(false, e.getMessage(), new ProductTagCacheModel());
        }
    }

    public FetchAllProductTagModel findAllProductTag(String organisationId, Integer productId) {
        try {
            String key = PRODUCT_TAG_KEY + "_" + organisationId + "_" + productId;
            Map<Object, Object> productTagrMap = redisTemplate.opsForHash().entries(key);

            if (!productTagrMap.isEmpty()) {
                List<ProductTagCacheModel> productTag = productTagrMap.values().stream()
                        .map(value -> objectMapper.convertValue(value, ProductTagCacheModel.class))
                        .collect(Collectors.toList());
                return new FetchAllProductTagModel(true, "Product Tage fetched successfully", productTag);
            }
            return new FetchAllProductTagModel(false, "No media found", Collections.emptyList());

        } catch (Exception e) {
            logger.error("Error fetching media for findAllProductTag {}: {}", organisationId, e.getMessage());
            return new FetchAllProductTagModel(false, e.getMessage(), Collections.emptyList());
        }
    }








}
