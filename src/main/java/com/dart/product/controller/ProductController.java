package com.dart.product.controller;

import com.dart.product.dto_model.product_dto_model.AllProductResDto;
import com.dart.product.dto_model.product_dto_model.ProductReqDTO;
import com.dart.product.dto_model.product_dto_model.ProductResModelDTO;
import com.dart.product.service.product.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/products")
public class ProductController {

    private final CreateProductService createProductService;
    private final UpdateProductService updateProductService;
    private final DeleteProductService deleteProductService;
    private final GetAllProductService getAllProductService;
    private final GetProductService getProductByIdService;

    public ProductController(
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

    /**
     * Endpoint to create a new product.
     * @param authToken Authorization token from request header.
     * @param reqModel Request body containing product details.
     * @return ResponseEntity with created ProductResModelDTO.
     */
    @PostMapping
    public ResponseEntity<ProductResModelDTO> createProduct(
            @RequestHeader("Authorization") String authToken,
            @RequestBody ProductReqDTO reqModel
    ) {
        return createProductService.createProduct(authToken, reqModel);
    }

    /**
     * Endpoint to update an existing product by ID.
     * @param authToken Authorization token from request header.
     * @param reqModel Request body containing updated product details.
     * @param id Path variable for product ID to update.
     * @return ResponseEntity with updated ProductResModelDTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResModelDTO> updateProduct(
            @RequestHeader("Authorization") String authToken,
            @RequestBody ProductReqDTO reqModel,
            @PathVariable Integer id
    ) {
        return updateProductService.updateProduct(authToken, reqModel, id);
    }

    /**
     * Endpoint to delete a product by ID.
     * @param authToken Authorization token from request header.
     * @param id Path variable for product ID to delete.
     * @return ResponseEntity with a confirmation or error message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResModelDTO> deleteProduct(
            @RequestHeader("Authorization") String authToken,
            @PathVariable Integer id
    ) {
        return deleteProductService.deleteProduct(authToken, id);
    }

    /**
     * Endpoint to retrieve all products.
     * @param authToken Authorization token from request header.
     * @return ResponseEntity containing a list of all products.
     */
    @GetMapping
    public ResponseEntity<AllProductResDto> getAllProducts(
            @RequestHeader("Authorization") String authToken
    ) {
        return getAllProductService.retrieveAllProduct(authToken);
    }

    /**
     * Endpoint to retrieve a single product by ID.
     * @param authToken Authorization token from request header.
     * @param id Path variable for product ID to retrieve.
     * @return ResponseEntity with the requested ProductResModelDTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResModelDTO> getProductById(
            @RequestHeader("Authorization") String authToken,
            @PathVariable Integer id
    ) {
        return getProductByIdService.retrieveProduct(authToken, id);
    }

}
