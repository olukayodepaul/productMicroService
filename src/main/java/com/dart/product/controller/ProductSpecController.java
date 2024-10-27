package com.dart.product.controller;


import com.dart.product.entity.product_specification_model.AddProductSpecReqModel;
import com.dart.product.entity.product_specification_model.AddProductSpecResModel;
import com.dart.product.entity.product_specification_model.FetchAllProductSpecResModel;
import com.dart.product.service.product_specification.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductSpecController {

    private final AddProductSpecService addProductSpecService;
    private final UpdateProductSpecService updateProductSpecService;
    private final DeleteProductSpecService deleteProductSpecService;
    private final GetOneProductSpecService getOneProductSpecService;
    private final GetAllProductSpecService getAllProductSpecService;

    public ProductSpecController(
            AddProductSpecService addProductSpecService,
            UpdateProductSpecService updateProductSpecService,
            DeleteProductSpecService deleteProductSpecService,
            GetOneProductSpecService getOneProductSpecService,
            GetAllProductSpecService getAllProductSpecService
    )
    {
        this.addProductSpecService = addProductSpecService;
        this.updateProductSpecService = updateProductSpecService;
        this.deleteProductSpecService = deleteProductSpecService;
        this.getOneProductSpecService = getOneProductSpecService;
        this.getAllProductSpecService = getAllProductSpecService;

    }

    @PostMapping("/products/specifications")
    public ResponseEntity<AddProductSpecResModel> addProductSpec(
            @RequestBody AddProductSpecReqModel reqBody,
            @RequestHeader("Authorization") String token)
    {
        return addProductSpecService.addProductSpec(reqBody, token);
    }

    @PutMapping("/products/specifications/{id}")
    public ResponseEntity<AddProductSpecResModel> updateProductSpec(
            @RequestBody AddProductSpecReqModel reqBody,
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Integer id)
    {
        return updateProductSpecService.updateProductSpec(reqBody, token, id);
    }

    @DeleteMapping("/products/{product_id}/specifications/{id}")
    public ResponseEntity<AddProductSpecResModel> deleteProductSpec(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Integer id,
            @PathVariable("product_id") Integer productId //this is newly added
    )
    {
        return deleteProductSpecService.updateProductSpec( id, token);
    }

    @GetMapping("/products/{product_id}/specifications/{id}")
    public ResponseEntity<AddProductSpecResModel> getOneProductSpec(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Integer id,
            @PathVariable("product_id") Integer productId)
    {
        return getOneProductSpecService.getOneProductSpec(id, productId, token);
    }

    @GetMapping("/products/{product_id}/specifications")
    public ResponseEntity<FetchAllProductSpecResModel> getAllProductSpec(
            @RequestHeader("Authorization") String token,
            @PathVariable("product_id") Integer productId)
    {
        return getAllProductSpecService.getAllProductSpec(productId, token);
    }
}
