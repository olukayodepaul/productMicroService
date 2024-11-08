package com.dart.product.controller;


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
    private final GetProductMediaService getProductMediaService;
    private final GetSpecificProductMediaService getSpecificProductMediaService;

    public ProductMediaController(
            CreateProductMediaService createProductMediaService,
            UpdateProductMediaService updateProductMediaService,
            UpdatePrimaryProductService updatePrimaryProductService,
            GetProductMediaService getProductMediaService,
            GetSpecificProductMediaService getSpecificProductMediaService
    ) {
        this.createProductMediaService = createProductMediaService;
        this.updateProductMediaService = updateProductMediaService;
        this.updatePrimaryProductService = updatePrimaryProductService;
        this.getProductMediaService = getProductMediaService;
        this.getSpecificProductMediaService = getSpecificProductMediaService;
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
    ){
        return getSpecificProductMediaService.getSpecificProductMediaMediaType(authToken, productId, mediaType);
    }




//
//    @GetMapping("/product_media/product_id/{product_id}/media_type/{media_type}")
//    public ResponseEntity<GetSpecMediaModel> getProductMediaByMediaType(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("product_id") Integer productId,
//            @PathVariable("media_type") String mediaType
//    ){
//        return getSpecificProductMediaService.getProductMediaByMediaType(token, productId, mediaType);
//    }
//
//    @GetMapping("/product_media/product_id/{product_id}/media_id/{media_id}")
//    public ResponseEntity<GetIndividualProductMediaModel> getProductMediaByMediaId(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("product_id") Integer productId,
//            @PathVariable("media_id") Integer mediaId
//    ){
//        return getIndividualProductMediaService.getProductMediaByMediaId(token, productId, mediaId);
//    }

//
////    private final String IMAGE_DIR = "/Users/upload/";
//
////    @GetMapping("/images/{filename:.+}")
////    @ResponseBody
////    public byte[] getImage(@PathVariable String filename) throws IOException {
////        System.out.println("bjbdbdjbjj "+filename);
////        File imageFile = new File(IMAGE_DIR + filename);
////        if (imageFile.exists()) {
////            InputStream in = new FileInputStream(imageFile);
////            return Files.readAllBytes(imageFile.toPath());
////        } else {
////            throw new IOException("Image not found");
////        }
////    }
//



    //    private final DeleteProductMediaService deleteProductMediaService;
//    private final FetchProductMediaService fetchProductMediaService;
//
//    private final GetOneProductMediaService getIndividualProductMediaService;

    //            DeleteProductMediaService deleteProductMediaService,
//            FetchProductMediaService fetchProductMediaService,
//            GetSpecificProductMediaService getSpecificProductMediaService,

    //        this.deleteProductMediaService = deleteProductMediaService;
//        this.fetchProductMediaService = fetchProductMediaService;
//        this.getSpecificProductMediaService = getSpecificProductMediaService;
//        this.getIndividualProductMediaService = getIndividualProductMediaService;
}
