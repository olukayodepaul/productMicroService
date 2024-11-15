package com.dart.product.dependency.di;

import com.dart.product.service.product.*;

public interface ProductControllerDi
{
    CreateProductService createProductService();
    UpdateProductService updateProductService();
    DeleteProductService deleteProductService();
    GetAllProductService getAllProductService();
    GetProductService getProductByIdService();
}
