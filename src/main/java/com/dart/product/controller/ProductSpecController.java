package com.dart.product.controller;


import com.dart.product.dto_model.product_specification_model.AddProductSpecReqModel;
import com.dart.product.dto_model.product_specification_model.AddProductSpecResModel;
import com.dart.product.dto_model.product_specification_model.FetchAllProductSpecResModel;
import com.dart.product.service.product_specification.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductSpecController {
//
//    private final AddProductSpecService addProductSpecService;
//    private final UpdateProductSpecService updateProductSpecService;
//    private final DeleteProductSpecService deleteProductSpecService;
//    private final GetOneProductSpecService getOneProductSpecService;
//
//    public ProductSpecController(
//            AddProductSpecService addProductSpecService,
//            UpdateProductSpecService updateProductSpecService,
//            DeleteProductSpecService deleteProductSpecService,
//            GetOneProductSpecService getOneProductSpecService
//    )
//    {
//        this.addProductSpecService = addProductSpecService;
//        this.updateProductSpecService = updateProductSpecService;
//        this.deleteProductSpecService = deleteProductSpecService;
//        this.getOneProductSpecService = getOneProductSpecService;
//
//    }
//
//    @PostMapping("/products/specifications")
//    public ResponseEntity<AddProductSpecResModel> addProductSpec(
//            @RequestBody AddProductSpecReqModel reqBody,
//            @RequestHeader("Authorization") String token)
//    {
//        return addProductSpecService.addProductSpec(reqBody, token);
//    }
//
//    @PutMapping("/products/specifications/{id}")
//    public ResponseEntity<AddProductSpecResModel> updateProductSpec(
//            @RequestBody AddProductSpecReqModel reqBody,
//            @RequestHeader("Authorization") String token,
//            @PathVariable("product_id") Integer productId,
//            @PathVariable("id") Integer id)
//    {
//        return updateProductSpecService.updateProductSpec(reqBody, token, productId, id);
//    }
//
//    @DeleteMapping("/products/{product_id}/specifications/{id}")
//    public ResponseEntity<AddProductSpecResModel> deleteProductSpec(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("id") Integer id,
//            @PathVariable("product_id") Integer productId
//    )
//    {
//        return deleteProductSpecService.updateProductSpec( id, token, productId);
//    }
//
//    @GetMapping("/products/{product_id}/specifications/{id}")
//    public ResponseEntity<AddProductSpecResModel> getOneProductSpec(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("id") Integer id,
//            @PathVariable("product_id") Integer productId)
//    {
//        return getOneProductSpecService.getOneProductSpec(id, productId, token);
//    }


}
