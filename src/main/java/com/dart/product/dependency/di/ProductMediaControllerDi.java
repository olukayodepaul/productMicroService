package com.dart.product.dependency.di;

import com.dart.product.service.product_media.*;

public interface ProductMediaControllerDi {
    CreateProductMediaService createProductMediaService();
    UpdateProductMediaService updateProductMediaService();
    UpdatePrimaryProductService updatePrimaryProductService();
    GetProductMediaByIdService getProductMediaService();
    GetSpecificProductMediaByProductIdService getSpecificProductMediaService();
    DeleteProductMediaService deleteProductMediaService();
    GetProductMediaByProductIdService getProductMediaByProductIdService();
    GetAllProductMediaByOrganisation getAllProductMediaByOrganisation();
}
