package com.dart.product.dependency.di;

import com.dart.product.service.product_feedback.CreateProductFeedBackService;
import com.dart.product.service.product_feedback.GetAllProductFeedBackService;
import com.dart.product.service.product_feedback.GetProductFeedBackService;

public interface ProductFeedBackControllerDi {

    CreateProductFeedBackService createProductFeedBackService();
    GetProductFeedBackService getProductFeedBackService();
    GetAllProductFeedBackService getAllProductFeedBackService();
}
