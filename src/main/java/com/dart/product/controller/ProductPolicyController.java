package com.dart.product.controller;


import com.dart.product.dto_model.product_policy_model.AddProductPolicyReqModel;
import com.dart.product.dto_model.product_policy_model.ProductPolicyAllResModel;
import com.dart.product.dto_model.product_policy_model.ProductPolicyOneResModel;
import com.dart.product.service.product_policies.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductPolicyController {

//    private final AddProductPolicyService addProductPolicyService;
//    private final UpdateProductPolicyService updateProductPolicyService;
//    private final DeleteProductPolicyService deleteProductPolicyService;
//    private final GetOneProductPolicyService getOneProductPolicyService;
//    private final GetAllProductPolicyService getAllProductPolicyService;
//
//    public ProductPolicyController(
//            AddProductPolicyService addProductPolicyService,
//            UpdateProductPolicyService updateProductPolicyService,
//            DeleteProductPolicyService deleteProductPolicyService,
//            GetOneProductPolicyService getOneProductPolicyService,
//            GetAllProductPolicyService getAllProductPolicyService
//    ) {
//        this.addProductPolicyService = addProductPolicyService;
//        this.updateProductPolicyService = updateProductPolicyService;
//        this.deleteProductPolicyService = deleteProductPolicyService;
//        this.getOneProductPolicyService = getOneProductPolicyService;
//        this.getAllProductPolicyService = getAllProductPolicyService;
//    }
//
//    @PostMapping("/product/policy")
//    public ResponseEntity<ProductPolicyOneResModel> addProductPolicy(
//            @RequestHeader("Authorization") String token,
//            @RequestBody AddProductPolicyReqModel reqBody
//    ) {
//        return addProductPolicyService.addProductPolicy(token, reqBody);
//    }
//
//
//    @PutMapping("/product/policy/{product_id}/{id}")
//    public ResponseEntity<ProductPolicyOneResModel> updateProductPolicy(
//            @RequestHeader("Authorization") String token,
//            @RequestBody AddProductPolicyReqModel reqBody,
//            @PathVariable("product_id") Integer productId,
//            @PathVariable("id") Integer id
//    ) {
//        return updateProductPolicyService.updateProductPolicy(token, reqBody, productId, id);
//    }
//
//    @DeleteMapping("/shipping/{product_id}/{id}/details")
//    public ResponseEntity<ProductPolicyOneResModel> deleteProductPolicy(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("product_id") Integer productId,
//            @PathVariable("id") Integer id
//
//    ) {
//        return deleteProductPolicyService.deleteProductPolicy(token, productId, id);
//    }
//
//    @GetMapping("/shipping/{product_id}/{id}/details")
//    public ResponseEntity<ProductPolicyOneResModel> getOneProductPolicy(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("id") Integer id,
//            @PathVariable("product_id") Integer productId) {
//        return getOneProductPolicyService.getOneProductPolicy(token, productId, id);
//    }
//
//    @GetMapping("/shipping/{product_id}/details")
//    public ResponseEntity<ProductPolicyAllResModel> getAllProductPolicy(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("product_id") Integer productId) {
//        return getAllProductPolicyService.getAllProductPolicy(token, productId);
//    }

}
