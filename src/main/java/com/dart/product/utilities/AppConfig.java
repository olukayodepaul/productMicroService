package com.dart.product.utilities;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    public static final int PAGINATION_LIMIT  = 20;
    public static final String NULL_VALIDATION = "cannot be null";
    public static final String ACCESS_TOKEN_VALIDATION = "Access Token";
    public static final String PRODUCT_NAME_VALIDATION = "Product Name";
    public static final String PRODUCT_DESCRIPTION_VALIDATION = "Description";
    public static final String PRODUCT_PRICE_VALIDATION = "Price";
    public static final String PRODUCT_DISCOUNT_VALIDATION = "Discount";
    public static final String PRODUCT_CATEGORY_VALIDATION = "Product Category";
    public static final String PRODUCT_ID_VALIDATION = "Product Id";
    public static final String EMPTY_PRODUCT_NAME_VALIDATION = "Product Name cant be empty";
    public static final String EMPTY_PRODUCT_DESCRIPTION_VALIDATION = "Product description cant be empty";
    public static final String INVALID_PRODUCT_PRICE = "Invalid product price";
    public static final String INVALID_PRODUCT_CATEGORY = "Invalid product category";
    public static final String INVALID_PRODUCT_BRAND_ID = "Invalid product brand id";
    public static final String INVALID_PRODUCT_ID = "Invalid product id";


    //user role protection response
    public static final String PERMISSION_VALIDATION = "You do not have permission to perform this action.";

    //bruteForce Protection
    public static final String BRUTE_FORCE_PROTECTION_RATE_LIMIT = "Rate limit exceeded";
    public static final String BRUTE_FORCE_PROTECTION_RESPONSE = "You have exceeded the maximum number of requests per minute.";
    public static final String CREATE_PRODUCT_BRUTE_FORCE_PROTECTION = "create_product_brute_force_protection";
    public static final String UPDATE_PRODUCT_BRUTE_FORCE_PROTECTION = "update_product_brute_force_protection";
    public static final String DELETE_PRODUCT_BRUTE_FORCE_PROTECTION = "delete_product_brute_force_protection";
    public static final String GET_PRODUCT_BRUTE_FORCE_PROTECTION = "get_product_brute_force_protection";
    public static final String GET_ALL_PRODUCT_BRUTE_FORCE_PROTECTION = "get_all_product_brute_force_protection";
    public static final String CREATE_PRODUCT_COMMENT_BRUTE_FORCE_PROTECTION = "create_product_comment_brute_force_protection";
    public static final String UPDATE_PRODUCT_COMMENT_BRUTE_FORCE_PROTECTION = "update_product_comment_brute_force_protection";
    public static final String DELETE_PRODUCT_COMMENT_BRUTE_FORCE_PROTECTION = "delete_product_comment_brute_force_protection";
    public static final String GET_PRODUCT_COMMENT_BRUTE_FORCE_PROTECTION = "get_product_comment_brute_force_protection";
    public static final String GET_ALL_PRODUCT_COMMENT_BRUTE_FORCE_PROTECTION = "get_all_product_comment_brute_force_protection";
    public static final String CREATE_PRODUCT_FEEDBACK_BRUTE_FORCE_PROTECTION = "create_product_feedback_brute_force_protection";
    public static final String GET_PRODUCT_FEEDBACK_BRUTE_FORCE_PROTECTION = "get_product_feed_back_brute_force_protection";
    public static final String GET_ALL_PRODUCT_FEEDBACK_BRUTE_FORCE_PROTECTION = "get_all_product_feed_back_brute_force_protection";

    //product
    public static final String CREATE_PRODUCT_RESPONSE = "product successfully created";
    public static final String UPDATE_PRODUCT_RESPONSE = "product successfully updated";
    public static final String DELETE_PRODUCT_RESPONSE = "product successfully deleted";
    public static final String GET_PRODUCT_RESPONSE = "Products fetched successfully";

    //share
    public static final String INVALID_RESOURCES_RESPONSE = "resource you are about to fetch does not exist";
    public static final String DELETE_RESOURCES_RESPONSE = "resource you are about to delete does not exist";
    public static final String UPDATE_RESOURCES_RESPONSE = "resource you are about to update does not exist";

    //product comment
    public static final String EMPTY_PRODUCT_COMMENT_VALIDATION = "Product comment cant be empty";
    public static final String PRODUCT_COMMENT_VALIDATION = "Comment text";
    public static final String PRODUCT_COMMENT_ID_VALIDATION = "Comment Id";
    public static final String INVALID_COMMENT_ID_VALIDATION = "Invalid comment id";
    public static final String VALID_GET_PRODUCT_COMMENT_RESPONSE = "product comment successfully fetch";
    public static final String PRODUCT_COMMENT_SUCCESSFULLY_CREATED = "product comment created successfully";
    public static final String PRODUCT_COMMENT_SUCCESSFULLY_DELETED = "product comment successfully deleted";

    //product feedback
    public static final String PRODUCT_FEEDBACK_TYPE_VALIDATION = "Feed back type";
    public static final String PRODUCT_FEEDBACK_SUCCESSFULLY_CREATED = "product feedback created successfully";
    public static final String PRODUCT_COMMENT_LIKE_DISLIKE_VALIDATION  = "Product comment cant be empty, can either be like, dislike or neutral";
    public static final String VALID_GET_PRODUCT_FEED_BACK_RESPONSE = "product feed back successfully fetched";





























    public static final String FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION ="";
    public static final String DELETED_MEDIA_ERROR_RESPONSE = "resource you are about to delete does not exist";


    //uploaded size
    public static  long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5 MB for images
    public static  long MAX_VIDEO_SIZE = 50 * 1024 * 1024; // 50 MB for videos
    public static  String MAX_SIZE_TAG = "Size Error"; // 50 MB for videos
    public static  String MAX_SIZE_IMAGE = "Image size exceeds the limit of 5 MB."; // 50 MB for videos
    public static  String MAX_SIZE_VIDEO = "Video size exceeds the limit of 50 MB.";

}
