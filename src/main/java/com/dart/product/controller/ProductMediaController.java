package com.dart.product.controller;


import com.dart.product.dependency.di.ProductMediaControllerDi;
import com.dart.product.dto_model.product_media_model.*;
import com.dart.product.service.product_media.*;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/api/products")
public class ProductMediaController {

    private final CreateProductMediaService createProductMediaService;
    private final UpdateProductMediaService updateProductMediaService;
    private final UpdatePrimaryProductService updatePrimaryProductService;
    private final GetProductMediaByIdService getProductMediaService;
    private final GetSpecificProductMediaByProductIdService getSpecificProductMediaService;
    private final DeleteProductMediaService deleteProductMediaService;
    private final GetProductMediaByProductIdService getProductMediaByProductIdService;
    private final GetAllProductMediaByOrganisation getAllProductMediaByOrganisation;

    public ProductMediaController(
            ProductMediaControllerDi di
    ) {
        this.createProductMediaService = di.createProductMediaService();
        this.updateProductMediaService = di.updateProductMediaService();
        this.updatePrimaryProductService = di.updatePrimaryProductService();
        this.getProductMediaService = di.getProductMediaService();
        this.getSpecificProductMediaService = di.getSpecificProductMediaService();
        this.deleteProductMediaService = di.deleteProductMediaService();
        this.getProductMediaByProductIdService = di.getProductMediaByProductIdService();
        this.getAllProductMediaByOrganisation = di.getAllProductMediaByOrganisation();
    }

    @PostMapping("/{product_id}/media")
    public ResponseEntity<ProductMediaResDTO> createProductMedia(
            @RequestHeader("Authorization") String authToken,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @PathVariable("product_id") Integer productId
    ) throws IOException {
        // File validation
        if (file == null || file.isEmpty()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), AppConfig.UPLOAD_FILE_RESPONSE),
                    HttpStatus.BAD_REQUEST
            );
        }
        return createProductMediaService.createProductMedia(authToken, file, productId);
    }

    @PutMapping("/{product_id}/media/{id}")
    public ResponseEntity<ProductMediaResDTO> updateProductMedia(
            @RequestHeader("Authorization") String authToken,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @PathVariable("product_id") Integer productId,
            @PathVariable("id") Integer id
    ) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), AppConfig.UPLOAD_FILE_RESPONSE),
                    HttpStatus.BAD_REQUEST
            );
        }
        return updateProductMediaService.updateProductMedia(authToken, file, productId, id);
    }

    @PutMapping("/{product_id}/primary/media/{id}")
    public ResponseEntity<PrimaryProductResDTO> updateProductMedia(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("product_id") Integer productId,
            @PathVariable("id") Integer id
    ) {
        return updatePrimaryProductService.updatePrimaryMedia(authToken, productId, id);
    }

    @GetMapping("/{product_id}/media/{id}")
    public ResponseEntity<ProductMediaResDTO> getProductMedia(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("product_id") Integer productId,
            @PathVariable("id") Integer id
    ) {
        return getProductMediaService.getProductMediaByMediaId(authToken, productId, id);
    }

    @GetMapping("/{product_id}/specific/media/{media_type}")
    public ResponseEntity<GetSpecMediaDTO> getSpecificProductMedia(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("product_id") Integer productId,
            @PathVariable("media_type") String mediaType
    ) {
        return getSpecificProductMediaService.getSpecificProductMediaMediaType(authToken, productId, mediaType);
    }

    @GetMapping("/{product_id}/media/all")
    public ResponseEntity<GetAllMediaDTO> getProductMediaByMediaId(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("product_id") Integer productId
    ) {
        return getProductMediaByProductIdService.getProductMediaByProductId(authToken, productId);
    }

    @GetMapping("/media")
    public ResponseEntity<ProductMediaResponseDTO> getProductMediaByOrganisation(
            @RequestHeader("Authorization") String authToken,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return getAllProductMediaByOrganisation.fetchProductMediaByOrganisation(authToken, offset, limit);
    }

    @DeleteMapping("/media/{id}")
    public ResponseEntity<ProductMediaResDTO> deleteProductMedia(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("id") Integer id
    ) {
        return deleteProductMediaService.deleteProductMedia(authToken, id);
    }



}