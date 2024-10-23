package com.dart.product.di;

import com.dart.product.service.product_media.MediaService;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductMediaRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.SaveAndUpdateRecord;
import com.dart.product.utilities.UtilitiesManager;
import com.dart.product.utilities.ValidationUtils;


public interface ServiceLocator {

    UtilitiesManager getUtilitiesManager();

    ValidationUtils getValidationUtils();

    FilterService getJwtService();

    MediaService getMediaService();

    ProductMappers getProductMappers();

    SaveAndUpdateRecord getSaveAndUpdateRecord();

    RedisProductCacheRepo getRedisProductCacheRepo();

    ProductMediaRepo getProductMediaRepo();
}