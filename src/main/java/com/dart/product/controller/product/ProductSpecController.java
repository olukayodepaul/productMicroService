package com.dart.product.controller.product;


import com.dart.product.entity.product_specification.AddProductSpecResModel;
import com.dart.product.service.product_specification.AddProductSpecService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductSpecController {

    private final AddProductSpecService addProductSpecService;

    public ProductSpecController(AddProductSpecService addProductSpecService) {
        this.addProductSpecService = addProductSpecService;
    }

    @PostMapping("/product-specifications")
    public ResponseEntity<String> addProductSpec(
            @RequestBody AddProductSpecResModel reqBody,
            @RequestHeader("Authorization") String token)
    {
        return addProductSpecService.addProductSpec(reqBody, token);
    }

}
