package com.dart.product.di;

import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.*;
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
    private ProductSpecificationRepo productSpecificationRepo;
    private ShippingDetailsRepo shippingDetailsRepo;
    private ProductsRepo productsRepo;
    private ProductPolicyRepo productPolicyRepo;
    private ProductReviewRepo productReviewRepo;
    private RelatedProductsRepo relatedProductsRepo;
    private SpecialOffersRepo specialOffersRepo;
    private ProductTagRepo productTagRepo;
    private ProductCommentRepo productCommentRepo;
    private ProductFeedBackRepo productFeedBackRepo;





    public ServiceLocatorImpl(
            UtilitiesManager utilitiesManager,
            ValidationUtils validationUtils,
            FilterService jwtService,
            MediaService mediaService,
            ProductMappers productMappers,
            SaveAndUpdateRecord saveAndUpdateRecord,
            RedisProductCacheRepo redisProductCacheRepo,
            ProductMediaRepo productMediaRepo,
            ProductSpecificationRepo productSpecificationRepo,
            ShippingDetailsRepo shippingDetailsRepo,
            ProductsRepo productsRepo,
            ProductPolicyRepo productPolicyRepo,
            ProductReviewRepo productReviewRepo,
            RelatedProductsRepo relatedProductsRepo,
            SpecialOffersRepo specialOffersRepo,
            ProductTagRepo productTagRepo,
            ProductCommentRepo productCommentRepo,
            ProductFeedBackRepo productFeedBackRepo
    )
    {
        this.utilitiesManager = utilitiesManager;
        this.validationUtils = validationUtils;
        this.jwtService = jwtService;
        this.mediaService = mediaService;
        this.productMappers = productMappers;
        this.saveAndUpdateRecord = saveAndUpdateRecord;
        this.redisProductCacheRepo = redisProductCacheRepo;
        this.productMediaRepo = productMediaRepo;
        this.productSpecificationRepo = productSpecificationRepo;
        this.shippingDetailsRepo = shippingDetailsRepo;
        this.productsRepo = productsRepo;
        this.productPolicyRepo = productPolicyRepo;
        this.productReviewRepo = productReviewRepo;
        this.relatedProductsRepo = relatedProductsRepo;
        this.specialOffersRepo = specialOffersRepo;
        this.productTagRepo =productTagRepo;
        this.productCommentRepo = productCommentRepo;
        this.productFeedBackRepo = productFeedBackRepo;
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
    public ProductsRepo getProductsRepo() {
        return productsRepo;
    }

    @Override
    public ProductMediaRepo getProductMediaRepo() {
        return productMediaRepo;
    }

    @Override
    public ProductSpecificationRepo getProductSpecificationRepo() {
        return productSpecificationRepo;
    }

    @Override
    public ShippingDetailsRepo getShippingDetailsRepo() {
        return shippingDetailsRepo;
    }

    @Override
    public ProductPolicyRepo getProductPolicyRepo() {
        return productPolicyRepo;
    }

    @Override
    public ProductReviewRepo getProductReviewRepo() {
        return productReviewRepo;
    }

    @Override
    public RelatedProductsRepo getRelatedProductsDbModel() {
        return relatedProductsRepo;
    }

    @Override
    public SpecialOffersRepo getSpecialOffersRepo() {
        return specialOffersRepo;
    }

    @Override
    public ProductTagRepo getProductTagRepo() {
        return productTagRepo;
    }

    @Override
    public ProductCommentRepo getProductCommentRepo() {
        return productCommentRepo;
    }

    @Override
    public ProductFeedBackRepo getProductFeedBackRepo() {
        return productFeedBackRepo;
    }

}