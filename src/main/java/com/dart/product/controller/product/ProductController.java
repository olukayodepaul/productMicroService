package com.dart.product.controller.product;


import com.dart.product.entity.product_model.AllProductResModel;
import com.dart.product.entity.product_model.ProductReqModel;
import com.dart.product.entity.product_model.ProductResModel;
import com.dart.product.service.product.*;
import com.dart.product.utilities.ResponseHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    public final ProductService productService;
    public final UpdateProductService updateProductService;
    public final DeleteProductService deleteProductService;
    public final GetAllProductService getAllProductService;
    public final GetProductByIdService getProductByIdService;

    public ProductController(
            ProductService productService,
            UpdateProductService updateProductService,
            DeleteProductService deleteProductService,
            GetAllProductService getAllProductService,
            GetProductByIdService getProductByIdService
    ) {
        this.productService = productService;
        this.updateProductService = updateProductService;
        this.deleteProductService = deleteProductService;
        this.getAllProductService = getAllProductService;
        this.getProductByIdService = getProductByIdService;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResModel> createProduct(
            @RequestBody ProductReqModel reqModel,
            @RequestHeader("Authorization") String token)
    {
        return productService.createProduct(reqModel, token);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResModel> updateProduct(
            @RequestBody ProductReqModel reqModel,
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Integer id
            )
    {
        return updateProductService.updateProduct(reqModel, token, id);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ResponseHandler> deleteProduct(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id)
    {
        return deleteProductService.deleteProduct(token, id);
    }

    @GetMapping("/products")
    public ResponseEntity<AllProductResModel> getAllProduct(
            @RequestHeader("Authorization") String token)
    {
        return getAllProductService.getAllProduct(token);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResModel> getProductById(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id
    )
    {
        return getProductByIdService.getProductById(token, id);
    }

}
