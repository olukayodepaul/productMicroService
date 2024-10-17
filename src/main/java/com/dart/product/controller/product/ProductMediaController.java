package com.dart.product.controller.product;


import com.dart.product.entity.product_media_model.ProductMediaResModel;
import com.dart.product.service.product_media.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ProductMediaController {

    private final AddProductMediaService addProductMediaService;

    public ProductMediaController(AddProductMediaService addProductMediaService) {
        this.addProductMediaService = addProductMediaService;
    }

    @PostMapping("/product-media")
    public ResponseEntity<ProductMediaResModel> uploadProductMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("data") String data,
            @RequestHeader("Authorization") String token

    ) {
        return addProductMediaService.addProductMedia(file, data, token);
    }

}
