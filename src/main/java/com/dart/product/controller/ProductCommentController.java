package com.dart.product.controller;

import com.dart.product.dependency.di.ProductCommentControllerDi;
import com.dart.product.dto_model.product_comments_model.AddProductCommentReqlDTO;
import com.dart.product.dto_model.product_comments_model.AllProductCommentAllResDTO;
import com.dart.product.dto_model.product_comments_model.ProductCommentResDTO;
import com.dart.product.service.product_comments.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/api/products")
public class ProductCommentController {

    private final CreateProductCommentsService createProductCommentsService;
    private final UpdateProductCommentsService updateProductCommentsService;
    private final DeleteProductCommentsService deleteProductCommentsService;
    private final GetProductCommentsService getProductCommentsService;
    private final GetAllProductCommentsService getAllProductCommentsService;

    ProductCommentController(
            ProductCommentControllerDi di
    )
    {
        this.createProductCommentsService = di.createProductCommentsService();
        this.updateProductCommentsService = di.updateProductCommentsService();
        this.deleteProductCommentsService = di.deleteProductCommentsService();
        this.getProductCommentsService = di.getProductCommentsService();
        this.getAllProductCommentsService = di.getAllProductCommentsService();
    }


    @PostMapping("/{product_id}/comments")
    public ResponseEntity<ProductCommentResDTO> createProductComment(
            @RequestHeader("Authorization") String authToken,
            @RequestBody AddProductCommentReqlDTO reqModel,
            @PathVariable("product_id") Integer productId
    ) {
        return createProductCommentsService.createProductComment(authToken, reqModel, productId);
    }

    @PutMapping("/{product_id}/comments/{id}")
    public ResponseEntity<ProductCommentResDTO> updateProductComment(
            @RequestHeader("Authorization") String authToken,
            @RequestBody AddProductCommentReqlDTO reqModel,
            @PathVariable("product_id") Integer productId,
            @PathVariable Integer id
    ) {
        return updateProductCommentsService.updateProductComment(authToken, reqModel, productId, id);
    }

    @DeleteMapping("/{product_id}/comments/{id}")
    public ResponseEntity<ProductCommentResDTO> deleteProductComment(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("product_id") Integer productId,
            @PathVariable Integer id
    ) {
        return deleteProductCommentsService.deleteProductComment(authToken, productId, id);
    }

    @GetMapping("/{product_id}/comments/{id}")
    public ResponseEntity<ProductCommentResDTO> getProductComment(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("product_id") Integer productId,
            @PathVariable Integer id
    ) {
        return getProductCommentsService.getProductComment(authToken, productId, id);
    }

    @GetMapping("/{product_id}/comments")
    public ResponseEntity<AllProductCommentAllResDTO> getAllProductComment(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("product_id") Integer productId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return getAllProductCommentsService.getAllProductComment(authToken, productId,  offset, limit);
    }

}
