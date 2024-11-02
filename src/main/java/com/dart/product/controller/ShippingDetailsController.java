package com.dart.product.controller;

import com.dart.product.dto_model.shipping_details_model.AddShippingDetailsReqModel;
import com.dart.product.dto_model.shipping_details_model.ShippingDetailsAllResModel;
import com.dart.product.dto_model.shipping_details_model.ShippingDetailsOneResModel;
import com.dart.product.service.shipping_details.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ShippingDetailsController {

//    private final AddShippingDetailsService addShippingDetailsService;
//    private final UpdateShippingDetailsService updateShippingDetailsService;
//    private final DeleteShippingDetailsService deleteShippingDetailsService;
//    private final GetOneShippingDetailsService getOneShippingDetailsService;
//    private final GetAllShippingDetailsService getAllShippingDetailsService;
//
//    public ShippingDetailsController(
//            AddShippingDetailsService addShippingDetailsService,
//            UpdateShippingDetailsService updateShippingDetailsService,
//            DeleteShippingDetailsService deleteShippingDetailsService,
//            GetOneShippingDetailsService getOneShippingDetailsService,
//            GetAllShippingDetailsService getAllShippingDetailsService
//    ) {
//        this.addShippingDetailsService = addShippingDetailsService;
//        this.updateShippingDetailsService = updateShippingDetailsService;
//        this.deleteShippingDetailsService = deleteShippingDetailsService;
//        this.getOneShippingDetailsService = getOneShippingDetailsService;
//        this.getAllShippingDetailsService = getAllShippingDetailsService;
//    }
//
//    @PostMapping("/shipping/details")
//    public ResponseEntity<ShippingDetailsOneResModel> addShippingDetails(
//            @RequestHeader("Authorization") String token,
//            @RequestBody AddShippingDetailsReqModel reqBody
//    ) {
//        return addShippingDetailsService.addShippingDetails(token, reqBody);
//    }
//
//    @PutMapping("/shipping/details/{product_id}/{id}")
//    public ResponseEntity<ShippingDetailsOneResModel> updateShippingDetails(
//            @RequestHeader("Authorization") String token,
//            @RequestBody AddShippingDetailsReqModel reqBody,
//            @PathVariable("product_id") Integer productId,
//            @PathVariable("id") Integer id
//    ) {
//        return updateShippingDetailsService.updateShippingDetails(token, reqBody, productId, id);
//    }
//
//    @DeleteMapping("/shipping/{product_id}/{id}/details")
//    public ResponseEntity<ShippingDetailsOneResModel> deleteShippingDetails(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("product_id") Integer productId,
//            @PathVariable("id") Integer id
//
//    ) {
//        return deleteShippingDetailsService.deleteShippingDetails(token, productId, id);
//    }
//
//    @GetMapping("/shipping/{product_id}/{id}/details")
//    public ResponseEntity<ShippingDetailsOneResModel> getOneShippingDetails(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("id") Integer id,
//            @PathVariable("product_id") Integer productId) {
//        return getOneShippingDetailsService.getOneShippingDetails(token, productId, id);
//    }
//
//    @GetMapping("/shipping/{product_id}/details")
//    public ResponseEntity<ShippingDetailsAllResModel> getAllShippingDetails(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("product_id") Integer productId) {
//        return getAllShippingDetailsService.getAllShippingDetails(token, productId);
//    }
}
