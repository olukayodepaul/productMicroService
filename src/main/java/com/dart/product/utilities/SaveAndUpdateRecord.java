package com.dart.product.utilities;


import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_media_model.MediaDbModel;
import com.dart.product.entity.product_media_model.SaveAndUpdateMediaResponse;
import com.dart.product.entity.product_model.ProductReqModel;
import com.dart.product.entity.product_model.ProductDbModel;
import com.dart.product.entity.product_specification_model.ProductSpecificationDbModel;
import com.dart.product.entity.product_specification_model.SaveAndUpdateProductSpecResponse;
import com.dart.product.entity.shipping_details_model.SaveAndUpdateShippingDetailsResponse;
import com.dart.product.entity.shipping_details_model.ShippingDetailsDbModel;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductMediaRepo;
import com.dart.product.repository.ProductSpecificationRepo;
import com.dart.product.repository.ProductsRepo;
import com.dart.product.repository.ShippingDetailsRepo;
import org.springframework.stereotype.Service;


@Service
public class SaveAndUpdateRecord {

    private final ServiceLocator serviceLocator;

    public SaveAndUpdateRecord(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public SaveAndUpdateResponse updateProductRecord(ProductDbModel regDetails) {
        try {
            return new SaveAndUpdateResponse(true, "", serviceLocator.getProductsRepo().save(regDetails)) ;
        } catch (Exception e) {
            //logger.error("DbSaveUpdatedService::updateProductRecord: {}", e.getMessage());
            return new SaveAndUpdateResponse(false, e.getMessage(), ProductDbModel.builder().build());
        }
    }

    public SaveAndUpdateMediaResponse saveProductMedia(MediaDbModel regDetails) {
        try {
            return new SaveAndUpdateMediaResponse(true, "", serviceLocator.getProductMediaRepo().save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateMediaResponse(false, e.getMessage(), MediaDbModel.builder().build());
        }
    }

    public SaveAndUpdateProductSpecResponse saveProductSpecification(ProductSpecificationDbModel regDetails) {
        try {
            return new SaveAndUpdateProductSpecResponse(true, "", serviceLocator.getProductSpecificationRepo().save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateProductSpecResponse(false, e.getMessage(), ProductSpecificationDbModel.builder().build());
        }
    }

    public SaveAndUpdateShippingDetailsResponse saveShippingDetails(ShippingDetailsDbModel regDetails) {
        try {
            return new SaveAndUpdateShippingDetailsResponse(true, "", serviceLocator.getShippingDetailsRepo().save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateShippingDetailsResponse(false, e.getMessage(), ShippingDetailsDbModel.builder().build());
        }
    }


}
