package com.dart.product.di;

import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductMediaRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.service.product_media.MediaService;
import com.dart.product.utilities.SaveAndUpdateRecord;
import com.dart.product.utilities.UtilitiesManager;
import com.dart.product.utilities.ValidationUtils;
import org.springframework.stereotype.Component;


@Component
public class ServiceLocatorImpl implements ServiceLocator {


    private UtilitiesManager utilitiesManager;
    private ValidationUtils validationUtils;
    private FilterService jwtService;
    private MediaService mediaService;
    private ProductMappers productMappers;
    private SaveAndUpdateRecord saveAndUpdateRecord;
    private RedisProductCacheRepo redisProductCacheRepo;
    private ProductMediaRepo productMediaRepo;

    public ServiceLocatorImpl(
            UtilitiesManager utilitiesManager,
            ValidationUtils validationUtils,
            FilterService jwtService,
            MediaService mediaService,
            ProductMappers productMappers,
            SaveAndUpdateRecord saveAndUpdateRecord,
            RedisProductCacheRepo redisProductCacheRepo,
            ProductMediaRepo productMediaRepo)
    {
        this.utilitiesManager = utilitiesManager;
        this.validationUtils = validationUtils;
        this.jwtService = jwtService;
        this.mediaService = mediaService;
        this.productMappers = productMappers;
        this.saveAndUpdateRecord = saveAndUpdateRecord;
        this.redisProductCacheRepo = redisProductCacheRepo;
        this.productMediaRepo = productMediaRepo;
    }

    @Override
    public UtilitiesManager getUtilitiesManager() {
        return utilitiesManager;
    }

    @Override
    public ValidationUtils getValidationUtils() {
        return validationUtils;
    }

    @Override
    public FilterService getJwtService() {
        return jwtService;
    }

    @Override
    public MediaService getMediaService() {
        return mediaService;
    }

    @Override
    public ProductMappers getProductMappers() {
        return productMappers;
    }

    @Override
    public SaveAndUpdateRecord getSaveAndUpdateRecord() {
        return saveAndUpdateRecord;
    }

    @Override
    public RedisProductCacheRepo getRedisProductCacheRepo() {
        return redisProductCacheRepo;
    }

    @Override
    public ProductMediaRepo getProductMediaRepo() {
        return productMediaRepo;
    }

}