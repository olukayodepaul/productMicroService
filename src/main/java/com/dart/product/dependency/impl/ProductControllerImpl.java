package com.dart.product.dependency.impl;

import com.dart.product.dependency.di.ProductControllerDi;
import com.dart.product.service.product.*;
import org.springframework.stereotype.Component;

@Component
public class ProductControllerImpl implements ProductControllerDi
{

    private final CreateProductService createProductService;
    private final UpdateProductService updateProductService;
    private final DeleteProductService deleteProductService;
    private final GetAllProductService getAllProductService;
    private final GetProductService getProductByIdService;

    public ProductControllerImpl(
            CreateProductService createProductService,
            UpdateProductService updateProductService,
            DeleteProductService deleteProductService,
            GetAllProductService getAllProductService,
            GetProductService getProductByIdService
    ) {
        this.createProductService = createProductService;
        this.updateProductService = updateProductService;
        this.deleteProductService = deleteProductService;
        this.getAllProductService = getAllProductService;
        this.getProductByIdService = getProductByIdService;
    }

    @Override
    public CreateProductService createProductService() {
        return createProductService;
    }

    @Override
    public UpdateProductService updateProductService() {
        return updateProductService;
    }

    @Override
    public DeleteProductService deleteProductService() {
        return deleteProductService;
    }

    @Override
    public GetAllProductService getAllProductService() {
        return getAllProductService;
    }

    @Override
    public GetProductService getProductByIdService() {
        return getProductByIdService;
    }
}
