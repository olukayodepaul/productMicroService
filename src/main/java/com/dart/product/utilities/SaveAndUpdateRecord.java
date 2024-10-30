package com.dart.product.utilities;


import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_comments_model.ProductCommentDbModel;
import com.dart.product.entity.product_comments_model.SaveAndUpdateProductCommentResponse;
import com.dart.product.entity.product_feedback.ProductFeedBackDbModel;
import com.dart.product.entity.product_feedback.SaveAndUpdateProductFeedBackResponse;
import com.dart.product.entity.product_media_model.MediaDbModel;
import com.dart.product.entity.product_media_model.SaveAndUpdateMediaResponse;
import com.dart.product.entity.product_model.ProductReqModel;
import com.dart.product.entity.product_model.ProductDbModel;
import com.dart.product.entity.product_policy_model.ProductPolicyDbModel;
import com.dart.product.entity.product_policy_model.SaveAndUpdateProductPolicyResponse;
import com.dart.product.entity.product_reviews_model.ProductReviewDbModel;
import com.dart.product.entity.product_reviews_model.SaveAndUpdateProductReviewResponse;
import com.dart.product.entity.product_specification_model.ProductSpecificationDbModel;
import com.dart.product.entity.product_specification_model.SaveAndUpdateProductSpecResponse;
import com.dart.product.entity.product_tags_model.ProductTagDbModel;
import com.dart.product.entity.product_tags_model.SaveAndUpdateProductTagResponse;
import com.dart.product.entity.related_products_model.RelatedProductsDbModel;
import com.dart.product.entity.related_products_model.SaveAndUpdateRelatedProductResponse;
import com.dart.product.entity.shipping_details_model.SaveAndUpdateShippingDetailsResponse;
import com.dart.product.entity.shipping_details_model.ShippingDetailsDbModel;
import com.dart.product.entity.special_offers_model.SaveAndUpdateSpecialOffersResponse;
import com.dart.product.entity.special_offers_model.SpecialOffersDbModel;
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

    public SaveAndUpdateProductPolicyResponse saveProductPolicy(ProductPolicyDbModel regDetails) {
        try {
            return new SaveAndUpdateProductPolicyResponse(true, "", serviceLocator.getProductPolicyRepo().save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateProductPolicyResponse(false, e.getMessage(), ProductPolicyDbModel.builder().build());
        }
    }

    //product_review
    public SaveAndUpdateProductReviewResponse saveProductReview(ProductReviewDbModel regDetails) {
        try {
            return new SaveAndUpdateProductReviewResponse(true, "", serviceLocator.getProductReviewRepo().save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateProductReviewResponse(false, e.getMessage(), ProductReviewDbModel.builder().build());
        }
    }

    //related_products
    public SaveAndUpdateRelatedProductResponse saveRelatedProduct(RelatedProductsDbModel regDetails) {
        try {
            return new SaveAndUpdateRelatedProductResponse(true, "", serviceLocator.getRelatedProductsDbModel().save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateRelatedProductResponse(false, e.getMessage(), RelatedProductsDbModel.builder().build());
        }
    }

    //special_offers
    public SaveAndUpdateSpecialOffersResponse saveSpecialOffer(SpecialOffersDbModel regDetails) {
        try {
            return new SaveAndUpdateSpecialOffersResponse(true, "", serviceLocator.getSpecialOffersRepo().save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateSpecialOffersResponse(false, e.getMessage(), SpecialOffersDbModel.builder().build());
        }
    }

    //product_tags
    public SaveAndUpdateProductTagResponse saveProductTag(ProductTagDbModel regDetails) {
        try {
            return new SaveAndUpdateProductTagResponse(true, "", serviceLocator.getProductTagRepo().save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateProductTagResponse(false, e.getMessage(), ProductTagDbModel.builder().build());
        }
    }

    //product_comments
    public SaveAndUpdateProductCommentResponse saveProductComment(ProductCommentDbModel regDetails) {
        try {
            return new SaveAndUpdateProductCommentResponse(true, "", serviceLocator.getProductCommentRepo().save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateProductCommentResponse(false, e.getMessage(), ProductCommentDbModel.builder().build());
        }
    }

    //product_feedback
    public SaveAndUpdateProductFeedBackResponse saveProductFeedBack(ProductFeedBackDbModel regDetails) {
        try {
            return new SaveAndUpdateProductFeedBackResponse(true, "", serviceLocator.getProductFeedBackRepo().save(regDetails)) ;
        } catch (Exception e) {
            return new SaveAndUpdateProductFeedBackResponse(false, e.getMessage(), ProductFeedBackDbModel.builder().build());
        }
    }

}
