package com.dart.product.dependency.di;

import com.dart.product.service.product_comments.*;

public interface ProductCommentControllerDi {
    CreateProductCommentsService createProductCommentsService();
    UpdateProductCommentsService updateProductCommentsService();
    DeleteProductCommentsService deleteProductCommentsService();
    GetProductCommentsService getProductCommentsService();
    GetAllProductCommentsService getAllProductCommentsService();
}
