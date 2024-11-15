package com.dart.product.dependency.di;

import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.UtilitiesManager;
import com.dart.product.utilities.ValidationUtils;


public interface ServicesDi {
    ValidationUtils validationUtils();
    RedisProductCacheRepo redisProductCacheRepo();
    FilterService jwtService();
    UtilitiesManager utilitiesManager();
    ProductMappers productMappers();
}
