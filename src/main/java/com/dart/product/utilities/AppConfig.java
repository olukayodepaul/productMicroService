package com.dart.product.utilities;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    //bruce force protection
    public static final String ADD_PRODUCT_BRUTE_FORCE_PROTECTION = "add_product_brute_force_protection";
    public static final String UPDATE_PRODUCT_BRUTE_FORCE_PROTECTION = "update_product_brute_force_protection";
    public static final String DELETE_PRODUCT_BRUTE_FORCE_PROTECTION = "delete_product_brute_force_protection";
    public static final String FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION = "fetch_all_product_brute_force_protection";
    public static final String FETCH_ALL_PRODUCT_MEDIA = "No media found for the given product ID.";


    //TAG
    public static final String UPDATE_PRODUCT_ERROR_TAG = "Validation error";
    public static final String GET_ALL_PRODUCT_ERROR_TAG = "Validation error";
    public static final String DELETE_PRODUCT_ERROR_TAG = "Validation error";


    //ROUTE RESPONSE
    public static final String UPDATE_PRODUCT_ERROR_RESPONSE = "";
    public static final String UPDATE_PRODUCT_RESPONSE = "Product successful updated";
    public static final String DELETE_PRODUCT_SUCCESS_RESPONSE = "Product successful deleted";
    public static final String FETCH_ALL_PRODUCT_RESPONSE = "No product found";
    public static final String DELETE_PRODUCT_RESPONSE = "Product can not be update";

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
