package com.dart.product.dependency.impl;

import com.dart.product.dependency.di.ServicesDi;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.UtilitiesManager;
import com.dart.product.utilities.ValidationUtils;
import org.springframework.stereotype.Component;

@Component
public class ServicesImpl implements ServicesDi {

    private ValidationUtils validationUtils;
    private RedisProductCacheRepo redisProductCacheRepo;
    private FilterService jwtService;
    private UtilitiesManager utilitiesManager;
    private ProductMappers productMappers;

    private ServicesImpl(
            ValidationUtils validationUtils,
            RedisProductCacheRepo redisProductCacheRepo,
            FilterService jwtService,
            UtilitiesManager utilitiesManager,
            ProductMappers productMappers
    ){
        this.validationUtils = validationUtils;
        this.redisProductCacheRepo = redisProductCacheRepo;
        this.jwtService = jwtService;
        this.utilitiesManager = utilitiesManager;
        this.productMappers = productMappers;
    }

    @Override
    public ValidationUtils validationUtils() {
        return validationUtils;
    }

    @Override
    public RedisProductCacheRepo redisProductCacheRepo() {
        return redisProductCacheRepo;
    }

    @Override
    public FilterService jwtService() {
        return jwtService;
    }

    @Override
    public UtilitiesManager utilitiesManager() {
        return utilitiesManager;
    }

    @Override
    public ProductMappers productMappers() {
        return productMappers;
    }
}
