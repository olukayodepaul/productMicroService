package com.dart.product.dependency.impl;

import com.dart.product.dependency.di.ProductMediaControllerDi;
import com.dart.product.service.product_media.*;
import org.springframework.stereotype.Component;

@Component
public class ProductMediaControllerImpl implements ProductMediaControllerDi {

    private final CreateProductMediaService createProductMediaService;
    private final UpdateProductMediaService updateProductMediaService;
    private final UpdatePrimaryProductService updatePrimaryProductService;
    private final GetProductMediaByIdService getProductMediaService;
    private final GetSpecificProductMediaByProductIdService getSpecificProductMediaService;
    private final DeleteProductMediaService deleteProductMediaService;
    private final GetProductMediaByProductIdService getProductMediaByProductIdService;
    private final GetAllProductMediaByOrganisation getAllProductMediaByOrganisation;

    public ProductMediaControllerImpl(
            CreateProductMediaService createProductMediaService,
            UpdateProductMediaService updateProductMediaService,
            UpdatePrimaryProductService updatePrimaryProductService,
            GetProductMediaByIdService getProductMediaService,
            GetSpecificProductMediaByProductIdService getSpecificProductMediaService,
            DeleteProductMediaService deleteProductMediaService,
            GetProductMediaByProductIdService getProductMediaByProductIdService,
            GetAllProductMediaByOrganisation getAllProductMediaByOrganisation

    ) {
        this.createProductMediaService = createProductMediaService;
        this.updateProductMediaService = updateProductMediaService;
        this.updatePrimaryProductService = updatePrimaryProductService;
        this.getProductMediaService = getProductMediaService;
        this.getSpecificProductMediaService = getSpecificProductMediaService;
        this.deleteProductMediaService = deleteProductMediaService;
        this.getProductMediaByProductIdService = getProductMediaByProductIdService;
        this.getAllProductMediaByOrganisation = getAllProductMediaByOrganisation;
    }

    @Override
    public CreateProductMediaService createProductMediaService() {
        return createProductMediaService;
    }

    @Override
    public UpdateProductMediaService updateProductMediaService() {
        return updateProductMediaService;
    }

    @Override
    public UpdatePrimaryProductService updatePrimaryProductService() {
        return updatePrimaryProductService;
    }

    @Override
    public GetProductMediaByIdService getProductMediaService() {
        return getProductMediaService;
    }

    @Override
    public GetSpecificProductMediaByProductIdService getSpecificProductMediaService() {
        return getSpecificProductMediaService;
    }

    @Override
    public DeleteProductMediaService deleteProductMediaService() {
        return deleteProductMediaService;
    }

    @Override
    public GetProductMediaByProductIdService getProductMediaByProductIdService() {
        return getProductMediaByProductIdService;
    }

    @Override
    public GetAllProductMediaByOrganisation getAllProductMediaByOrganisation() {
        return getAllProductMediaByOrganisation;
    }
}
