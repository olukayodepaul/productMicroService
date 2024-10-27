package com.dart.product.controller;

import com.dart.product.entity.shipping_details_model.AddShippingDetailsReqModel;
import com.dart.product.entity.shipping_details_model.ShippingDetailsResModel;
import com.dart.product.service.shipping_details.AddShippingDetailsService;
import com.dart.product.service.shipping_details.UpdateShippingDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ShippingDetailsController {


    public final AddShippingDetailsService addShippingDetailsService;
    public final UpdateShippingDetailsService updateShippingDetailsService;

    public ShippingDetailsController(AddShippingDetailsService addShippingDetailsService, UpdateShippingDetailsService updateShippingDetailsService) {
        this.addShippingDetailsService = addShippingDetailsService;
        this.updateShippingDetailsService = updateShippingDetailsService;
    }

        @PostMapping("/shipping/details")
        public ResponseEntity<ShippingDetailsResModel> addShippingDetails(
                @RequestBody AddShippingDetailsReqModel reqBody,
                @RequestHeader("Authorization") String token)
        {
            return addShippingDetailsService.addShippingDetails(reqBody, token);
        }

        @PutMapping("/shipping/details/{id}")
        public ResponseEntity<ShippingDetailsResModel> updateProductSpec(
                @RequestBody AddShippingDetailsReqModel reqBody,
                @RequestHeader("Authorization") String token,
                @PathVariable("id") Integer id
        )
        {
            return updateShippingDetailsService.updateShippingDetails(reqBody, token, id);
        }


    //
    //    @DeleteMapping("/products/{product_id}/specifications/{id}")
    //    public ResponseEntity<AddProductSpecResModel> deleteProductSpec(
    //            @RequestHeader("Authorization") String token,
    //            @PathVariable("id") Integer id,
    //            @PathVariable("product_id") Integer productId //this is newly added
    //    )
    //    {
    //        return deleteProductSpecService.updateProductSpec( id, token);
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
    //
    //    @GetMapping("/products/{product_id}/specifications")
    //    public ResponseEntity<FetchAllProductSpecResModel> getAllProductSpec(
    //            @RequestHeader("Authorization") String token,
    //            @PathVariable("product_id") Integer productId)
    //    {
    //        return getAllProductSpecService.getAllProductSpec(productId, token);
    //    }
}
