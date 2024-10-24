package com.dart.product.utilities;


import com.dart.product.entity.product_media_model.MediaDbModel;
import com.dart.product.entity.product_media_model.SaveAndUpdateMediaResponse;
import com.dart.product.entity.product_model.ProductReqModel;
import com.dart.product.entity.product_model.ProductDbModel;
import com.dart.product.entity.product_specification.ProductSpecificationDbModel;
import com.dart.product.entity.product_specification.SaveAndUpdateProductSpecResponse;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductMediaRepo;
import com.dart.product.repository.ProductSpecificationRepo;
import com.dart.product.repository.ProductsRepo;
import org.springframework.stereotype.Service;


@Service
public class SaveAndUpdateRecord {

    private final ProductsRepo productsRepo;
    private final ProductMediaRepo productMediaRepo;
    private final ProductMappers mappers;
    private final ProductSpecificationRepo productSpecificationRepo;

    public SaveAndUpdateRecord(ProductsRepo productsRepo, ProductMediaRepo productMediaRepo, ProductMappers mappers, ProductSpecificationRepo productSpecificationRepo) {
        this.productsRepo = productsRepo;
        this.productMediaRepo = productMediaRepo;
        this.mappers = mappers;
        this.productSpecificationRepo = productSpecificationRepo;
    }

    public SaveAndUpdateResponse saveProductRecord(ProductReqModel regDetails) {
        try {
            return new SaveAndUpdateResponse(true, "", productsRepo.save(mappers.toProduct(regDetails))) ;
        } catch (Exception e) {
//          logger.error("DbSaveUpdatedService::saveProductRecord: {}", e.getMessage());
            return new SaveAndUpdateResponse(false, e.getMessage(), ProductDbModel.builder().build());
        }
    }

    public SaveAndUpdateResponse updateProductRecord(ProductDbModel regDetails) {
        try {
            return new SaveAndUpdateResponse(true, "", productsRepo.save(regDetails)) ;
        } catch (Exception e) {
//            logger.error("DbSaveUpdatedService::updateProductRecord: {}", e.getMessage());
            return new SaveAndUpdateResponse(false, e.getMessage(), ProductDbModel.builder().build());
        }
    }

    public SaveAndUpdateMediaResponse saveProductMedia(MediaDbModel regDetails) {
        try {
            return new SaveAndUpdateMediaResponse(true, "", productMediaRepo.save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateMediaResponse(false, e.getMessage(), MediaDbModel.builder().build());
        }
    }

    public SaveAndUpdateProductSpecResponse saveProductSpecification(ProductSpecificationDbModel regDetails) {
        try {
            return new SaveAndUpdateProductSpecResponse(true, "", productSpecificationRepo.save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateProductSpecResponse(false, e.getMessage(), ProductSpecificationDbModel.builder().build());
        }
    }


}
