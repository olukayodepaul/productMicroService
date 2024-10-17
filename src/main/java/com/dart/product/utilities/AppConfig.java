package com.dart.product.utilities;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    //bruce force protection
    public static final String ADD_PRODUCT_BRUTE_FORCE_PROTECTION = "add_product_brute_force_protection";
    public static final String UPDATE_PRODUCT_BRUTE_FORCE_PROTECTION = "update_product_brute_force_protection";
    public static final String DELETE_PRODUCT_BRUTE_FORCE_PROTECTION = "delete_product_brute_force_protection";
    public static final String FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION = "fetch_all_product_brute_force_protection";


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


    //Product Media
    public static final String ADD_PRODUCT_MEDIA = "Product media successfully created";

}
