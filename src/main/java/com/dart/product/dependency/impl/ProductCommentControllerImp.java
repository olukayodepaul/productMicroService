package com.dart.product.dependency.impl;

import com.dart.product.dependency.di.ProductCommentControllerDi;
import com.dart.product.service.product_comments.*;
import org.springframework.stereotype.Component;

@Component
public class ProductCommentControllerImp implements ProductCommentControllerDi {

    private final CreateProductCommentsService createProductCommentsService;
    private final UpdateProductCommentsService updateProductCommentsService;
    private final DeleteProductCommentsService deleteProductCommentsService;
    private final GetProductCommentsService getProductCommentsService;
    private final GetAllProductCommentsService getAllProductCommentsService;

    ProductCommentControllerImp(
            CreateProductCommentsService createProductCommentsService,
            UpdateProductCommentsService updateProductCommentsService,
            DeleteProductCommentsService deleteProductCommentsService,
            GetProductCommentsService getProductCommentsService,
            GetAllProductCommentsService getAllProductCommentsService
    ) {
        this.createProductCommentsService = createProductCommentsService;
        this.updateProductCommentsService = updateProductCommentsService;
        this.deleteProductCommentsService = deleteProductCommentsService;
        this.getProductCommentsService = getProductCommentsService;
        this.getAllProductCommentsService = getAllProductCommentsService;
    }

    @Override
    public CreateProductCommentsService createProductCommentsService() {
        return createProductCommentsService;
    }

    @Override
    public UpdateProductCommentsService updateProductCommentsService() {
        return updateProductCommentsService;
    }

    @Override
    public DeleteProductCommentsService deleteProductCommentsService() {
        return deleteProductCommentsService;
    }

    @Override
    public GetProductCommentsService getProductCommentsService() {
        return getProductCommentsService;
    }

    @Override
    public GetAllProductCommentsService getAllProductCommentsService() {
        return getAllProductCommentsService;
    }
}
