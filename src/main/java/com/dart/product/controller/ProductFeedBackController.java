package com.dart.product.controller;


import com.dart.product.dependency.di.ProductFeedBackControllerDi;
import com.dart.product.dto_model.product_feedback.AddProductFeedBackReqDTO;
import com.dart.product.dto_model.product_feedback.ProductFeedBackAllResDTO;
import com.dart.product.dto_model.product_feedback.ProductFeedBackResDTO;
import com.dart.product.service.product_feedback.CreateProductFeedBackService;
import com.dart.product.service.product_feedback.GetAllProductFeedBackService;
import com.dart.product.service.product_feedback.GetProductFeedBackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/api/products")
public class ProductFeedBackController {

    private final CreateProductFeedBackService createProductFeedBackService;
    private final GetProductFeedBackService getProductFeedBackService;
    private final GetAllProductFeedBackService getAllProductFeedBackService;

    public ProductFeedBackController(
           ProductFeedBackControllerDi di
    ) {
        this.createProductFeedBackService = di.createProductFeedBackService();
        this.getProductFeedBackService = di.getProductFeedBackService();
        this.getAllProductFeedBackService = di.getAllProductFeedBackService();
    }

    @PostMapping("/{product_id}/feedback")
    public ResponseEntity<ProductFeedBackResDTO> createProductFeedBack(
            @RequestHeader("Authorization") String authToken,
            @RequestBody AddProductFeedBackReqDTO reqModel,
            @PathVariable("product_id") Integer productId
    ) {
        return createProductFeedBackService.createProductFeedBack(authToken, reqModel, productId);
    }

    @GetMapping("/{product_id}/feedback")
    public ResponseEntity<ProductFeedBackResDTO> getProductFeedBack(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("product_id") Integer productId
    ) {
        return getProductFeedBackService.getProductFeedBack(authToken, productId);
    }

    @GetMapping("/feedback")
    public ResponseEntity<ProductFeedBackAllResDTO> getAllProductFeedBack(
            @RequestHeader("Authorization") String authToken,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return getAllProductFeedBackService.getAllProductFeedBack(authToken,  offset, limit);
    }

}
