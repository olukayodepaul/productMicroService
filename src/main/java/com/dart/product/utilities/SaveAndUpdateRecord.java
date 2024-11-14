package com.dart.product.utilities;


import com.dart.product.dto_model.product_policy_model.ProductPolicyDbModel;
import com.dart.product.dto_model.product_policy_model.SaveAndUpdateProductPolicyResponse;
import com.dart.product.dto_model.product_reviews_model.ProductReviewDbModel;
import com.dart.product.dto_model.product_reviews_model.SaveAndUpdateProductReviewResponse;
import com.dart.product.dto_model.product_specification_model.ProductSpecificationDbModel;
import com.dart.product.dto_model.product_specification_model.SaveAndUpdateProductSpecResponse;
import com.dart.product.dto_model.product_tags_model.ProductTagDbModel;
import com.dart.product.dto_model.product_tags_model.SaveAndUpdateProductTagResponse;
import com.dart.product.dto_model.related_products_model.RelatedProductsDbModel;
import com.dart.product.dto_model.related_products_model.SaveAndUpdateRelatedProductResponse;
import com.dart.product.dto_model.shipping_details_model.SaveAndUpdateShippingDetailsResponse;
import com.dart.product.dto_model.shipping_details_model.ShippingDetailsDbModel;
import com.dart.product.dto_model.special_offers_model.SaveAndUpdateSpecialOffersResponse;
import com.dart.product.dto_model.special_offers_model.SpecialOffersDbModel;
import com.dart.product.repository.*;
import org.springframework.stereotype.Service;


@Service
public class SaveAndUpdateRecord {

    private final ProductsRepo productsRepo;
    private final ProductMediaContentRepo productMediaRepo;
    private final ProductSpecificationRepo productSpecificationRepo;
    private final ShippingDetailsRepo shippingDetailsRepo;
    private final ProductPolicyRepo productPolicyRepo;
    private final ProductReviewRepo productReviewRepo;
    private final RelatedProductsRepo relatedProductsRepo;
    private final SpecialOffersRepo specialOffersRepo;
    private final ProductTagRepo productTagRepo;
    private final ProductCommentRepo productCommentRepo;
    private final ProductFeedBackRepo productFeedBackRepo;

    public SaveAndUpdateRecord(ProductsRepo productsRepo, ProductMediaContentRepo productMediaRepo, ProductSpecificationRepo productSpecificationRepo, ShippingDetailsRepo shippingDetailsRepo, ProductPolicyRepo productPolicyRepo, ProductReviewRepo productReviewRepo, RelatedProductsRepo relatedProductsRepo, SpecialOffersRepo specialOffersRepo, ProductTagRepo productTagRepo, ProductCommentRepo productCommentRepo, ProductFeedBackRepo productFeedBackRepo) {
        this.productsRepo = productsRepo;
        this.productMediaRepo = productMediaRepo;
        this.productSpecificationRepo = productSpecificationRepo;
        this.shippingDetailsRepo = shippingDetailsRepo;
        this.productPolicyRepo = productPolicyRepo;
        this.productReviewRepo = productReviewRepo;
        this.relatedProductsRepo = relatedProductsRepo;
        this.specialOffersRepo = specialOffersRepo;
        this.productTagRepo = productTagRepo;
        this.productCommentRepo = productCommentRepo;
        this.productFeedBackRepo = productFeedBackRepo;
    }

    //product




    public SaveAndUpdateProductSpecResponse saveProductSpecification(ProductSpecificationDbModel regDetails) {
        try {
            return new SaveAndUpdateProductSpecResponse(true, "", productSpecificationRepo.save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateProductSpecResponse(false, e.getMessage(), ProductSpecificationDbModel.builder().build());
        }
    }

    public SaveAndUpdateShippingDetailsResponse saveShippingDetails(ShippingDetailsDbModel regDetails) {
        try {
            return new SaveAndUpdateShippingDetailsResponse(true, "", shippingDetailsRepo.save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateShippingDetailsResponse(false, e.getMessage(), ShippingDetailsDbModel.builder().build());
        }
    }

    public SaveAndUpdateProductPolicyResponse saveProductPolicy(ProductPolicyDbModel regDetails) {
        try {
            return new SaveAndUpdateProductPolicyResponse(true, "", productPolicyRepo.save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateProductPolicyResponse(false, e.getMessage(), ProductPolicyDbModel.builder().build());
        }
    }

    //product_review
    public SaveAndUpdateProductReviewResponse saveProductReview(ProductReviewDbModel regDetails) {
        try {
            return new SaveAndUpdateProductReviewResponse(true, "", productReviewRepo.save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateProductReviewResponse(false, e.getMessage(), ProductReviewDbModel.builder().build());
        }
    }

    //related_products
    public SaveAndUpdateRelatedProductResponse saveRelatedProduct(RelatedProductsDbModel regDetails) {
        try {
            return new SaveAndUpdateRelatedProductResponse(true, "", relatedProductsRepo.save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateRelatedProductResponse(false, e.getMessage(), RelatedProductsDbModel.builder().build());
        }
    }

    //special_offers
    public SaveAndUpdateSpecialOffersResponse saveSpecialOffer(SpecialOffersDbModel regDetails) {
        try {
            return new SaveAndUpdateSpecialOffersResponse(true, "", specialOffersRepo.save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateSpecialOffersResponse(false, e.getMessage(), SpecialOffersDbModel.builder().build());
        }
    }

    //product_tags
    public SaveAndUpdateProductTagResponse saveProductTag(ProductTagDbModel regDetails) {
        try {
            return new SaveAndUpdateProductTagResponse(true, "", productTagRepo.save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateProductTagResponse(false, e.getMessage(), ProductTagDbModel.builder().build());
        }
    }



}
