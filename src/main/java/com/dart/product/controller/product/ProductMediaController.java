package com.dart.product.controller.product;


import com.dart.product.entity.product_media_model.*;
import com.dart.product.service.product_media.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductMediaController {

    private final AddProductMediaService addProductMediaService;
    private final UpdateProductMediaService updateProductMediaService;
    private final UpdatePrimaryProductService updatePrimaryProductService;
    private final DeleteProductMediaService deleteProductMediaService;
    private final FetchProductMediaService fetchProductMediaService;
    private final GetSpecificProductMediaService getSpecificProductMediaService;
    private final GetIndividualProductMediaService getIndividualProductMediaService;


    public ProductMediaController(
            AddProductMediaService addProductMediaService,
            UpdateProductMediaService updateProductMediaService,
            UpdatePrimaryProductService updatePrimaryProductService,
            DeleteProductMediaService deleteProductMediaService,
            FetchProductMediaService fetchProductMediaService,
            GetSpecificProductMediaService getSpecificProductMediaService,
            GetIndividualProductMediaService getIndividualProductMediaService

    )
    {
        this.addProductMediaService = addProductMediaService;
        this.updateProductMediaService = updateProductMediaService;
        this.updatePrimaryProductService = updatePrimaryProductService;
        this.deleteProductMediaService = deleteProductMediaService;
        this.fetchProductMediaService = fetchProductMediaService;
        this.getSpecificProductMediaService = getSpecificProductMediaService;
        this.getIndividualProductMediaService = getIndividualProductMediaService;

    }

    @PostMapping("/product-media")
    public ResponseEntity<ProductMediaResModel> uploadProductMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("data") String data,
            @RequestHeader("Authorization") String token
    ) {
        return addProductMediaService.addProductMedia(file, data, token);
    }

    //first uploaded image should be primary image. also to video
    @PutMapping("/product_media/media_id/{id}/media_url")
    public ResponseEntity<ProductMediaResModel> updateProductMedia(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Integer id
    ) throws IOException {
        return updateProductMediaService.updateProductMedia(file, token, id);
    }

    @PutMapping("/product_media/media_id/{id}/primary/listing")
    public ResponseEntity<PrimaryProductResModel> updateProductMedia(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Integer id
    ){
        return updatePrimaryProductService.updateProductPrimaryListing(token, id);
    }

    @DeleteMapping("/product_media/media_id/{id}")
    public ResponseEntity<ProductMediaResModel> deleteProductMedia(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Integer id
    ){
        return deleteProductMediaService.deleteProductMedia(token, id);
    }

    @GetMapping("/product_media/product_id/{product_id}")
    public ResponseEntity<GetAllMediaModel> getProductMedia(
            @RequestHeader("Authorization") String token,
            @PathVariable("product_id") Integer productId
    ){
        return fetchProductMediaService.getProductMedia(token, productId);
    }

    @GetMapping("/product_media/product_id/{product_id}/media_type/{media_type}")
    public ResponseEntity<GetSpecMediaModel> getProductMediaByMediaType(
            @RequestHeader("Authorization") String token,
            @PathVariable("product_id") Integer productId,
            @PathVariable("media_type") String mediaType
    ){
        return getSpecificProductMediaService.getProductMediaByMediaType(token, productId, mediaType);
    }

    @GetMapping("/product_media/product_id/{product_id}/media_id/{media_id}")
    public ResponseEntity<GetIndividualProductMediaModel> getProductMediaByMediaId(
            @RequestHeader("Authorization") String token,
            @PathVariable("product_id") Integer productId,
            @PathVariable("media_id") Integer mediaId
    ){
        return getIndividualProductMediaService.getProductMediaByMediaId(token, productId, mediaId);
    }


//    private final String IMAGE_DIR = "/Users/upload/";

//    @GetMapping("/images/{filename:.+}")
//    @ResponseBody
//    public byte[] getImage(@PathVariable String filename) throws IOException {
//        System.out.println("bjbdbdjbjj "+filename);
//        File imageFile = new File(IMAGE_DIR + filename);
//        if (imageFile.exists()) {
//            InputStream in = new FileInputStream(imageFile);
//            return Files.readAllBytes(imageFile.toPath());
//        } else {
//            throw new IOException("Image not found");
//        }
//    }




}
