package com.dart.product.controller;

import com.dart.product.dto_model.product_comments_model.AddProductCommentReqlDTO;
import com.dart.product.dto_model.product_comments_model.ProductCommentAllResDTO;
import com.dart.product.dto_model.product_comments_model.ProductCommentOneResDTO;
import com.dart.product.service.product_comments.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
public class ProductCommentController {

    private final CreateProductCommentsService createProductCommentsService;
    private final UpdateProductCommentsService updateProductCommentsService;
    private final DeleteProductCommentsService deleteProductCommentsService;
    private final GetProductCommentsService getProductCommentsService;
    private final GetAllProductCommentsService getAllProductCommentsService;

    ProductCommentController(
            CreateProductCommentsService createProductCommentsService,
            UpdateProductCommentsService updateProductCommentsService,
            DeleteProductCommentsService deleteProductCommentsService,
            GetProductCommentsService getProductCommentsService,
            GetAllProductCommentsService getAllProductCommentsService
    )
    {
        this.createProductCommentsService = createProductCommentsService;
        this.updateProductCommentsService = updateProductCommentsService;
        this.deleteProductCommentsService = deleteProductCommentsService;
        this.getProductCommentsService = getProductCommentsService;
        this.getAllProductCommentsService = getAllProductCommentsService;
    }


    @PostMapping("/{product_id}/comments")
    public ResponseEntity<ProductCommentOneResDTO> createProductComment(
            @RequestHeader("Authorization") String authToken,
            @RequestBody AddProductCommentReqlDTO reqModel,
            @PathVariable("product_id") Integer productId
    ) {
        return createProductCommentsService.createProductComment(authToken, reqModel, productId);
    }

    @PutMapping("/{product_id}/comments/{id}")
    public ResponseEntity<ProductCommentOneResDTO> updateProductComment(
            @RequestHeader("Authorization") String authToken,
            @RequestBody AddProductCommentReqlDTO reqModel,
            @PathVariable("product_id") Integer productId,
            @PathVariable Integer id
    ) {
        return updateProductCommentsService.updateProductComment(authToken, reqModel, productId, id);
    }

    @DeleteMapping("/{product_id}/comments/{id}")
    public ResponseEntity<ProductCommentOneResDTO> deleteProductComment(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("product_id") Integer productId,
            @PathVariable Integer id
    ) {
        return deleteProductCommentsService.deleteProductComment(authToken, productId, id);
    }

    @GetMapping("/{product_id}/comments/{id}")
    public ResponseEntity<ProductCommentOneResDTO> getProductComment(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("product_id") Integer productId,
            @PathVariable Integer id
    ) {
        return getProductCommentsService.getProductComment(authToken, productId, id);
    }


    @GetMapping("/{product_id}/comments")
    public ResponseEntity<ProductCommentAllResDTO> getAllProductComment(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("product_id") Integer productId
    ) {
        return getAllProductCommentsService.getAllProductComment(authToken, productId);
    }


}
