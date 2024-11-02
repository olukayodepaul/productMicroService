package com.dart.product.utilities;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /**
     * Here is the new refactor code for the application
     */
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

    //product
    public static final String CREATE_PRODUCT_RESPONSE = "product successfully created";
    public static final String UPDATE_PRODUCT_RESPONSE = "product successfully updated";
    public static final String DELETE_PRODUCT_RESPONSE = "product successfully deleted";
    public static final String PRODUCT_NOT_FOUND_ERROR_RESPONSE = "product not found.";
    public static final String GET_PRODUCT_RESPONSE = "Products fetched successfully";

    //share
    public static final String EMPTY_PRODUCT_USER_ID = "User id cant be empty";
    public static final String USER_ID_VALIDATION = "User id";

    //product comment
    public static final String EMPTY_PRODUCT_COMMENT_VALIDATION = "Product comment cant be empty";
    public static final String PRODUCT_COMMENT_VALIDATION = "Comment text";
    public static final String PRODUCT_COMMENT_ID_VALIDATION = "Comment Id";
    public static final String INVALID_COMMENT_ID_VALIDATION = "Invalid comment id";
    public static final String INVALID_COMMENT_ERROR_RESPONSE = "product ID and comment Id not found";
    public static final String VALID_GET_PRODUCT_COMMENT_RESPONSE = "product comment successfully fetch";
    public static final String INVALID_PRODUCT_COMMENT_ERROR_RESPONSE = "resource you are about to delete does not exist";




























    public static final String FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION ="";
    //bruce force protection
    public static final String ADD_PRODUCT_BRUTE_FORCE_PROTECTION = "add_product_brute_force_protection";
//    public static final String DELETE_PRODUCT_BRUTE_FORCE_PROTECTION = "delete_product_brute_force_protection";

    public static final String FETCH_ALL_PRODUCT_MEDIA = "No media found for the given product ID.";


    //TAG
    public static final String UPDATE_PRODUCT_ERROR_TAG = "Validation error";
    public static final String GET_ALL_PRODUCT_ERROR_TAG = "Validation error";
    public static final String DELETE_PRODUCT_ERROR_TAG = "Validation error";


    //ROUTE RESPONSE
    public static final String UPDATE_PRODUCT_ERROR_RESPONSE = "";
//    public static final String UPDATE_PRODUCT_RESPONSE = "Product successful updated";
    public static final String DELETE_PRODUCT_SUCCESS_RESPONSE = "Product successful deleted";
    public static final String FETCH_ALL_PRODUCT_RESPONSE = "No product found";
//    public static final String DELETE_PRODUCT_RESPONSE = "Product can not be update";

    public static final String DELETE_PRIMARY_MEDIA_ERROR_RESPONSE = "primary media cant be deleted";
    public static final String DELETED_MEDIA_ERROR_RESPONSE = "resource you are about to delete does not exist";


    //Product Media
    public static final String ADD_PRODUCT_MEDIA = "Product media successfully created";
    public static final String UPDATED_PRODUCT_MEDIA = "Update successful";


    //uploaded size
    public static  long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5 MB for images
    public static  long MAX_VIDEO_SIZE = 50 * 1024 * 1024; // 50 MB for videos
    public static  String MAX_SIZE_TAG = "Size Error"; // 50 MB for videos
    public static  String MAX_SIZE_IMAGE = "Image size exceeds the limit of 5 MB."; // 50 MB for videos
    public static  String MAX_SIZE_VIDEO = "Video size exceeds the limit of 50 MB.";

}
