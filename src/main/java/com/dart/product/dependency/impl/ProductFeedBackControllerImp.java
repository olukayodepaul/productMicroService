package com.dart.product.dependency.impl;

import com.dart.product.dependency.di.ProductFeedBackControllerDi;
import com.dart.product.service.product_feedback.CreateProductFeedBackService;
import com.dart.product.service.product_feedback.GetAllProductFeedBackService;
import com.dart.product.service.product_feedback.GetProductFeedBackService;
import org.springframework.stereotype.Component;


@Component
public class ProductFeedBackControllerImp implements ProductFeedBackControllerDi {

    private final CreateProductFeedBackService createProductFeedBackService;
    private final GetProductFeedBackService getProductFeedBackService;
    private final GetAllProductFeedBackService getAllProductFeedBackService;

    public ProductFeedBackControllerImp(
            CreateProductFeedBackService createProductFeedBackService,
            GetProductFeedBackService getProductFeedBackService,
            GetAllProductFeedBackService getAllProductFeedBackService
    ) {
        this.createProductFeedBackService = createProductFeedBackService;
        this.getProductFeedBackService = getProductFeedBackService;
        this.getAllProductFeedBackService = getAllProductFeedBackService;
    }


    @Override
    public CreateProductFeedBackService createProductFeedBackService() {
        return createProductFeedBackService;
    }

    @Override
    public GetProductFeedBackService getProductFeedBackService() {
        return getProductFeedBackService;
    }

    @Override
    public GetAllProductFeedBackService getAllProductFeedBackService() {
        return getAllProductFeedBackService;
    }
}
