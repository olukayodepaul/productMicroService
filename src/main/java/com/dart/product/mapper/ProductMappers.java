package com.dart.product.mapper;

import com.dart.product.entity.prodct_media.MediaContentDbEntity;
import com.dart.product.entity.prodct_media.MediaDbEntity;
import com.dart.product.entity.prodct_media.ProductContentMediaCacheEntity;
import com.dart.product.entity.prodct_media.ProductMediaCacheEntity;
import com.dart.product.entity.product_comment_entity.ProductCommentCacheEntity;
import com.dart.product.entity.product_comment_entity.ProductCommentDbEntity;
import com.dart.product.entity.product_entity.ProductCacheEntity;
import com.dart.product.entity.product_entity.ProductDbEntity;
import com.dart.product.dto_model.product_comments_model.*;
import com.dart.product.dto_model.product_feedback.*;
import com.dart.product.dto_model.product_media_model.*;
import com.dart.product.dto_model.product_dto_model.*;
import com.dart.product.dto_model.product_policy_model.*;
import com.dart.product.dto_model.product_reviews_model.*;
import com.dart.product.dto_model.product_specification_model.*;
import com.dart.product.dto_model.product_tags_model.*;
import com.dart.product.dto_model.related_products_model.*;
import com.dart.product.dto_model.shipping_details_model.*;
import com.dart.product.dto_model.special_offers_model.*;
import com.dart.product.entity.product_entity.ProductDbTrailEntity;
import com.dart.product.entity.product_feedback_entity.ProductFeedBackCacheEntity;
import com.dart.product.entity.product_feedback_entity.ProductFeedBackDbEntity;
import com.dart.product.dto_model.product_policy_model.CreateProductPolicyReqDTO;
import com.dart.product.dto_model.product_policy_model.ProductPolicyResDTO;
import com.dart.product.entity.product_policy_warranty_entity.ProductPolicyCacheEntity;
import com.dart.product.entity.product_policy_warranty_entity.ProductPolicyDbEntity;
import com.dart.product.utilities.UtilitiesManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductMappers {

    private final UtilitiesManager utilitiesManager;

    public ProductMappers(UtilitiesManager utilitiesManager) {
        this.utilitiesManager = utilitiesManager;
    }


    //product
    public AllProductResDTO.Product toProductDto(ProductDbEntity productDbEntity) {
        return AllProductResDTO.Product.builder()
                .id(productDbEntity.getId())
                .name(productDbEntity.getName())
                .description(productDbEntity.getDescription())
                .price(productDbEntity.getPrice())
                .currency(productDbEntity.getCurrency())
                .discount(productDbEntity.getDiscount())
                .category_id(productDbEntity.getCategory_id())
                .brand_id(productDbEntity.getBrand_id())
                .return_policy_id(productDbEntity.getReturn_policy_id())
                .warranty_policy_id(productDbEntity.getWarranty_policy_id())
                .is_active(productDbEntity.getIsActive())
                .updated_at(productDbEntity.getUpdated_at())
                .created_at(productDbEntity.getCreated_at())
                .build();
    }

    public ProductDbEntity toProduct(ProductReqDTO product) {
        return ProductDbEntity.builder()
                .id(product.getId())
                .organisationId(product.getOrganisation_id())
                .name(product.getName().toLowerCase())
                .description(product.getDescription().toLowerCase())
                .price(product.getPrice())
                .currency(product.getCurrency())
                .discount(product.getDiscount())
                .category_id(Integer.parseInt(product.getCategory_id()))
                .created_by(product.getCreated_by())
                .brand_id(Integer.parseInt(product.getBrand_id()))
                .warranty_policy_id(Integer.parseInt(product.getWarranty_policy_id()))
                .return_policy_id(Integer.parseInt(product.getReturn_policy_id()))
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .isActive(true)
                .build();
    }

    public ProductDbTrailEntity mapProductToProductTrail(ProductDbEntity product,String changeType, String oldNew, UUID consolidated){
        return ProductDbTrailEntity.builder()
                .products_id(product.getId())
                .organisationId(product.getOrganisationId())
                .created_updated_deleted_by(product.getCreated_by())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .currency(product.getCurrency())
                .discount(product.getDiscount())
                .category_id(product.getCategory_id())
                .brand_id(product.getBrand_id())
                .return_policy_id(product.getReturn_policy_id())
                .warranty_policy_id(product.getWarranty_policy_id())
                .change_type(changeType)
                .old_new_change(oldNew)
                .consolidated(consolidated)
                .isActive(product.getIsActive())
                .created_at(product.getCreated_at())
                .updated_at(LocalDateTime.now())
                .build();
    }

    public ProductCacheEntity toProductCache(ProductDbEntity product) {
        return ProductCacheEntity.builder()
                .id(product.getId())
                .organisation_id(product.getOrganisationId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .currency(product.getCurrency())
                .discount(product.getDiscount())
                .category_id(product.getCategory_id())
                .brand_id(product.getBrand_id())
                .warranty_policy_id(product.getWarranty_policy_id())
                .return_policy_id(product.getReturn_policy_id())
                .is_active(product.getIsActive())
                .updated_at(product.getUpdated_at())
                .created_at(product.getCreated_at())
                .build();
    }

    public ProductResModelDTO toProductResponseBuilder(ProductDbEntity reqModel, String message) {
        return ProductResModelDTO.builder()
                .status(true)
                .message(message)
                .product(
                        ProductResModelDTO.Product
                                .builder()
                                .id(reqModel.getId())
                                .name(reqModel.getName())
                                .description(reqModel.getDescription())
                                .price(reqModel.getPrice())
                                .currency(reqModel.getCurrency())
                                .discount(reqModel.getDiscount())
                                .category_id(reqModel.getCategory_id())
                                .brand_id(reqModel.getBrand_id())
                                .warranty_policy_id(reqModel.getWarranty_policy_id())
                                .return_policy_id(reqModel.getReturn_policy_id())
                                .created_at(reqModel.getCreated_at())
                                .updated_at(reqModel.getUpdated_at())
                                .is_active(reqModel.getIsActive())
                                .build()
                )
                .build();
    }


    public ProductDbEntity mapProductCacheToPersistence(ProductCacheEntity product) {
        return ProductDbEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .category_id(product.getCategory_id())
                .brand_id(product.getBrand_id())
                .created_at(product.getCreated_at())
                .updated_at(product.getUpdated_at())
                .isActive(product.getIs_active())
                .build();
    }

    public Page<ProductDbEntity> toCacheFromProduct(List<ProductCacheEntity> products, Pageable pageable) {
        List<ProductDbEntity> productDbEntities = products.stream()
                .map(product -> ProductDbEntity.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .organisationId(product.getOrganisation_id())
                        .price(product.getPrice())
                        .currency(product.getCurrency())
                        .discount(product.getDiscount())
                        .category_id(product.getCategory_id())
                        .brand_id(product.getBrand_id())
                        .return_policy_id(product.getReturn_policy_id())
                        .warranty_policy_id(product.getWarranty_policy_id())
                        .isActive(product.getIs_active())
                        .updated_at(product.getUpdated_at())
                        .created_at(product.getCreated_at())
                        .build())
                .collect(Collectors.toList());
        return new PageImpl<>(productDbEntities, pageable, products.size());
    }


    //Product Comment
    public ProductCommentDbEntity mapAddProductCommentModelToDbModel(AddProductCommentReqlDTO reqModel) {
        return ProductCommentDbEntity.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProduct_id())
                .organisationId(reqModel.getOrganisation_id())
                .userId(utilitiesManager.convertStringToUUID(reqModel.getUser_id()))
                .commentText(reqModel.getComment_text())
                .isActive(reqModel.is_active())
                .updatedAt(reqModel.getUpdated_at())
                .createdAt(reqModel.getCreated_at())
                .build();
    }

    public ProductCommentCacheEntity mapProductCommentDbModelToDbModel(ProductCommentDbEntity reqModel) {
        return ProductCommentCacheEntity.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .userId(reqModel.getUserId())
                .commentText(reqModel.getCommentText())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build();
    }

    public ProductCommentResDTO productCommentResponseBuilder(ProductCommentDbEntity reqModel, String message) {
        return ProductCommentResDTO.builder()
                .status(true)
                .message(message)
                .product_comments(
                        ProductCommentResDTO.ProductComment
                                .builder()
                                .id(reqModel.getId())
                                .product_id(reqModel.getProductId())
                                .organisation_id(reqModel.getOrganisationId())
                                .user_id(reqModel.getUserId())
                                .comment_text(reqModel.getCommentText())
                                .is_active(reqModel.isActive())
                                .updated_at(reqModel.getUpdatedAt())
                                .created_at(reqModel.getCreatedAt())
                                .build()
                )
                .build();
    }

    public ProductCommentDbEntity mapDbModelToProductCommentDbModel(ProductCommentCacheEntity reqModel) {
        return ProductCommentDbEntity.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .userId(reqModel.getUserId())
                .commentText(reqModel.getCommentText())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build();
    }

    public AllProductCommentAllResDTO.ProductComment mapToAllProductComment(ProductCommentDbEntity reqModel) {
        return AllProductCommentAllResDTO.ProductComment.builder()
                .id(reqModel.getId())
                .product_id(reqModel.getProductId())
                .organisation_id(reqModel.getOrganisationId())
                .user_id(reqModel.getUserId())
                .comment_text(reqModel.getCommentText())
                .is_active(reqModel.isActive())
                .updated_at(reqModel.getUpdatedAt())
                .created_at(reqModel.getCreatedAt())
                .build();
    }

    public Page<ProductCommentDbEntity> allProductComment(List<ProductCommentCacheEntity> reqModel, Pageable pageable) {
        List<ProductCommentDbEntity> productDbEntities = reqModel.stream()
                .map(productComment -> ProductCommentDbEntity.builder()
                        .id(productComment.getId())
                        .productId(productComment.getProductId())
                        .organisationId(productComment.getOrganisationId())
                        .userId(productComment.getUserId())
                        .commentText(productComment.getCommentText())
                        .isActive(productComment.isActive())
                        .updatedAt(productComment.getUpdatedAt())
                        .createdAt(productComment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return new PageImpl<>(productDbEntities, pageable, reqModel.size());
    }


    //Product Feedback
    public ProductFeedBackDbEntity mapAddProductFeedBackModelToDbModel(AddProductFeedBackReqDTO reqModel) {
        return ProductFeedBackDbEntity.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProduct_id())
                .organisationId(reqModel.getOrganisation_id())
                .userId(utilitiesManager.convertStringToUUID(reqModel.getUser_id()))
                .feedbackType(reqModel.getFeedback_type())
                .updatedAt(reqModel.getUpdated_at())
                .createdAt(reqModel.getCreated_at())
                .build();
    }

    public ProductFeedBackCacheEntity mapProductFeedBackDbModelToDbModel(ProductFeedBackDbEntity reqModel) {
        return ProductFeedBackCacheEntity.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .userId(reqModel.getUserId())
                .feedbackType(reqModel.getFeedbackType())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build();
    }

    public ProductFeedBackResDTO productFeedBackResponseBuilder(ProductFeedBackDbEntity reqModel, String message) {
        return ProductFeedBackResDTO.builder()
                .status(true)
                .message(message)
                .product_feedback(
                        ProductFeedBackResDTO.ProductFeedBack
                                .builder()
                                .id(reqModel.getId())
                                .product_id(reqModel.getProductId())
                                .organisation_id(reqModel.getOrganisationId())
                                .user_id(reqModel.getUserId())
                                .feedback_type(reqModel.getFeedbackType())
                                .updated_at(reqModel.getUpdatedAt())
                                .created_at(reqModel.getCreatedAt())
                                .build()
                )
                .build();
    }

    public ProductFeedBackDbEntity mapDbModelToProductFeedBackDbModel(ProductFeedBackCacheEntity reqModel) {
        return ProductFeedBackDbEntity.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .userId(reqModel.getUserId())
                .feedbackType(reqModel.getFeedbackType())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build();
    }

    public Page<ProductFeedBackDbEntity> mapToAllProductFeedBack(List<ProductFeedBackCacheEntity> reqModel, Pageable pageable) {
        List<ProductFeedBackDbEntity> productDbEntities = reqModel.stream()
                .map(productComment -> ProductFeedBackDbEntity.builder()
                        .id(productComment.getId())
                        .productId(productComment.getProductId())
                        .organisationId(productComment.getOrganisationId())
                        .userId(productComment.getUserId())
                        .feedbackType(productComment.getFeedbackType())
                        .updatedAt(productComment.getUpdatedAt())
                        .createdAt(productComment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return new PageImpl<>(productDbEntities, pageable, reqModel.size());
    }

    public ProductFeedBackAllResDTO.ProductFeedBack mapToAllProductFeedBack(ProductFeedBackDbEntity reqModel) {
        return ProductFeedBackAllResDTO.ProductFeedBack.builder()
                .id(reqModel.getId())
                .product_id(reqModel.getProductId())
                .organisation_id(reqModel.getOrganisationId())
                .user_id(reqModel.getUserId())
                .feedback_type(reqModel.getFeedbackType())
                .updated_at(reqModel.getUpdatedAt())
                .created_at(reqModel.getCreatedAt())
                .build();
    }

    //product media
    public ProductMediaResDTO toProductMediaResponse(MediaContentDbEntity productMedia, String message) {
        return ProductMediaResDTO.builder()
                .status(true)
                .message(message)
                .data(ProductMediaResDTO.ProductMedia
                        .builder()
                        .id(productMedia.getId())
                        .product_id(productMedia.getProductId())
                        .organisation_id(productMedia.getOrganisationId())
                        .media_type(productMedia.getMediaType())
                        .is_primary(productMedia.getIsPrimary())
                        .is_active(productMedia.getIsActive())
                        .media_url(productMedia.getMediaUrl())
                        .updated_at(productMedia.getUpdatedAt())
                        .created_at(productMedia.getCreatedAt())
                        .build())
                .build();
    }

    public ProductMediaResDTO toProductMediaToResDTO(ProductContentMediaCacheEntity productMedia, String message) {
        return ProductMediaResDTO.builder()
                .status(true)
                .message(message)
                .data(ProductMediaResDTO.ProductMedia
                        .builder()
                        .id(productMedia.getId())
                        .product_id(productMedia.getProduct_id())
                        .organisation_id(productMedia.getOrganisation_id())
                        .media_type(productMedia.getMedia_type())
                        .is_primary(productMedia.getIs_primary())
                        .is_active(productMedia.getIsActive())
                        .media_url(productMedia.getMedia_url())
                        .updated_at(productMedia.getUpdated_at())
                        .created_at(productMedia.getCreated_at())
                        .build())
                .build();
    }

    public MediaContentDbEntity toProductMedia(MediaUploadReqModel mediaData) {
        return MediaContentDbEntity.builder()
                .id(mediaData.getId())
                .product_media_id(mediaData.getProduct_media_id())
                .productId(mediaData.getProduct_id())
                .organisationId(mediaData.getOrganisation_id())
                .mediaType(mediaData.getMedia_type())
                .mediaUrl(mediaData.getMedia_url())
                .isPrimary(mediaData.isPrimary())
                .isActive(mediaData.getIsActive())
                .updatedAt(mediaData.getUpdated_at())
                .createdAt(mediaData.getCreated_at())
                .build();
    }

    public PrimaryProductResDTO productMediaBuilder(MediaContentDbEntity newMedia, MediaContentDbEntity oldMedia, String message) {
        return PrimaryProductResDTO.builder()
                .status(true)
                .message(message)
                .product_id(newMedia.getProductId())
                .media_type(newMedia.getMediaType())
                .is_active(newMedia.getIsActive())
                .organisation_id(newMedia.getOrganisationId())
                .current_primary_media(PrimaryProductResDTO.CurrentPrimaryMedia
                        .builder()
                        .id(newMedia.getId())
                        .is_primary(newMedia.getIsPrimary())
                        .media_url(newMedia.getMediaUrl())
                        .updated_at(newMedia.getUpdatedAt())
                        .created_at(newMedia.getCreatedAt())
                        .build()
                )
                .previous_primary_media(PrimaryProductResDTO.PreviousPrimaryMedia
                        .builder()
                        .id(oldMedia.getId())
                        .is_primary(oldMedia.getIsPrimary())
                        .media_url(oldMedia.getMediaUrl())
                        .updated_at(oldMedia.getUpdatedAt())
                        .created_at(oldMedia.getCreatedAt())
                        .build()
                )
                .build();
    }

    public GetSpecMediaDTO getAllSpecificMedia(List<ProductContentMediaCacheEntity> reqBody, String message) {
        return GetSpecMediaDTO.builder()
                .status(true)
                .message(message)
                .product_id(reqBody.getFirst().getProduct_id())
                .media_type(reqBody.getFirst().getMedia_type())
                .is_active(reqBody.getFirst().getIsActive())
                .product_media(reqBody.stream().map(spec -> GetSpecMediaDTO.Media
                        .builder()
                        .id(spec.getId())
                        .media_url(spec.getMedia_url())
                        .is_primary(spec.getIs_primary())
                        .updated_at(spec.getUpdated_at())
                        .created_at(spec.getCreated_at())
                        .build()).collect(Collectors.toList())
                )
                .build();
    }

    public ProductContentMediaCacheEntity toCacheProductMedia(MediaContentDbEntity productMedia) {
        return ProductContentMediaCacheEntity.builder()
                .id(productMedia.getId())
                .product_media_id(productMedia.getProduct_media_id())
                .product_id(productMedia.getProductId())
                .organisation_id(productMedia.getOrganisationId())
                .media_type(productMedia.getMediaType())
                .media_url(productMedia.getMediaUrl())
                .is_primary(productMedia.getIsPrimary())
                .isActive(productMedia.getIsActive())
                .updated_at(productMedia.getUpdatedAt())
                .created_at(productMedia.getCreatedAt())
                .build();
    }

    public List<ProductContentMediaCacheEntity> mapProductMedia(List<MediaContentDbEntity> productMedia) {
        return productMedia.stream().map(media -> ProductContentMediaCacheEntity
                .builder()
                .id(media.getId())
                .product_media_id(media.getProduct_media_id())
                .product_id(media.getProductId())
                .organisation_id(media.getOrganisationId())
                .media_type(media.getMediaType())
                .media_url(media.getMediaUrl())
                .is_primary(media.getIsPrimary())
                .isActive(media.getIsActive())
                .updated_at(media.getUpdatedAt())
                .created_at(media.getCreatedAt())
                .build()
        ).collect(Collectors.toList());
    }

    public List<MediaContentDbEntity> mapProductMediaCacheToProductDTO(List<ProductContentMediaCacheEntity> productMedia) {
        return productMedia.stream().map(media -> MediaContentDbEntity
                .builder()
                .id(media.getId())
                .productId(media.getProduct_id())
                .product_media_id(media.getProduct_id())
                .organisationId(media.getOrganisation_id())
                .mediaType(media.getMedia_type())
                .mediaUrl(media.getMedia_url())
                .isPrimary(media.getIs_primary())
                .isActive(media.getIsActive())
                .updatedAt(media.getUpdated_at())
                .createdAt(media.getCreated_at())
                .build()
        ).collect(Collectors.toList());
    }

    public List<MediaDbEntity> mapProductMediaCachePage(List<ProductMediaCacheEntity> productMedia) {
        return productMedia.stream().map(media -> MediaDbEntity
                .builder()
                .id(media.getId())
                .productId(media.getProduct_id())
                .organisationId(media.getOrganisation_id())
                .createdAt(media.getCreated_at())
                .build()

        ).collect(Collectors.toList());
    }

    public List<MediaContentDbEntity> mapToAllProductMediaToPage(List<ProductContentMediaCacheEntity> reqModel) {
        return reqModel.stream()
                .map(media -> MediaContentDbEntity.builder()
                        .id(media.getId())
                        .productId(media.getProduct_id())
                        .organisationId(media.getOrganisation_id())
                        .mediaType(media.getMedia_type())
                        .mediaUrl(media.getMedia_url())
                        .isPrimary(media.getIs_primary())
                        .isActive(media.getIsActive())
                        .updatedAt(media.getUpdated_at())
                        .createdAt(media.getCreated_at())
                        .build())
                .collect(Collectors.toList());
    }

    public GetAllMediaDTO mapProductMediaEntityProductDTO(List<MediaContentDbEntity> productMedia, String message) {
        List<MediaContentDbEntity> image = productMedia.stream()
                .filter(filter -> filter.getMediaType().equalsIgnoreCase("image"))
                .sorted(Comparator.comparing(MediaContentDbEntity::getId))
                .toList();
        List<MediaContentDbEntity> video = productMedia.stream()
                .filter(filter -> filter.getMediaType().equalsIgnoreCase("video"))
                .sorted(Comparator.comparing((MediaContentDbEntity::getId)))
                .toList();
        return GetAllMediaDTO.builder()
                .status(true)
                .message(message)
                .product_id(productMedia.getFirst().getProductId())
                .is_active(productMedia.getFirst().getIsActive())
                .image_media_type(
                        image.stream().map(image_spec -> GetAllMediaDTO.ImageMedia.builder()
                                        .id(image_spec.getId())
                                        .media_url(image_spec.getMediaUrl())
                                        .is_primary(image_spec.getIsPrimary())
                                        .updated_at(image_spec.getUpdatedAt())
                                        .created_at(image_spec.getCreatedAt())
                                        .build())
                                .collect(Collectors.toList())
                )
                .video_media_type(
                        video.stream().map(video_spec -> GetAllMediaDTO.VideoMedia.builder()
                                .id(video_spec.getId())
                                .media_url(video_spec.getMediaUrl())
                                .is_primary(video_spec.getIsPrimary())
                                .updated_at(video_spec.getUpdatedAt())
                                .created_at(video_spec.getCreatedAt())
                                .build()).collect(Collectors.toList())
                )
                .build();
    }

    public ProductMediaResponseDTO mapProductMediaByOrganisation(
            List<MediaContentDbEntity> productMedia,
            ProductMediaResponseDTO.PaginationMetadata pagination,
            String message) {

        return ProductMediaResponseDTO.builder()
                .status(true)
                .message(message)
                .product_media(productMedia.stream()
                        .collect(Collectors.groupingBy(MediaContentDbEntity::getProductId))
                        .entrySet()
                        .stream()
                        .map(entry -> {
                            Integer productId = entry.getKey();
                            List<MediaContentDbEntity> mediaList = entry.getValue();
                            return ProductMediaResponseDTO.ProductMedia.builder()
                                    .product_id(productId)
                                    .is_active(mediaList.getFirst().getIsActive())
                                    .image_media_type(mediaList.stream()
                                            .filter(media -> "image".equalsIgnoreCase(media.getMediaType()))
                                            .map(image -> ProductMediaResponseDTO.ProductMedia.ImageMedia.builder()
                                                    .id(image.getId())
                                                    .is_primary(image.getIsPrimary())
                                                    .media_type(image.getMediaType())
                                                    .media_url(image.getMediaUrl())
                                                    .updated_at(image.getUpdatedAt())
                                                    .created_at(image.getCreatedAt())
                                                    .build())
                                            .sorted(Comparator.comparing((ProductMediaResponseDTO.ProductMedia.ImageMedia::getId)))
                                            .collect(Collectors.toList()))
                                    .video_media_type(mediaList.stream()
                                            .filter(media -> "video".equalsIgnoreCase(media.getMediaType()))
                                            .map(video -> ProductMediaResponseDTO.ProductMedia.VideoMedia.builder()
                                                    .id(video.getId())
                                                    .is_primary(video.getIsPrimary())
                                                    .media_type(video.getMediaType())
                                                    .media_url(video.getMediaUrl())
                                                    .updated_at(video.getUpdatedAt())
                                                    .created_at(video.getCreatedAt())
                                                    .build())
                                            .sorted(Comparator.comparing((ProductMediaResponseDTO.ProductMedia.VideoMedia::getId)))
                                            .collect(Collectors.toList()))
                                    .build();
                        })
                        .sorted(Comparator.comparing((ProductMediaResponseDTO.ProductMedia::getProduct_id)))
                        .collect(Collectors.toList()))
                .pagination(pagination)
                .build();
    }

    public ProductMediaCacheEntity mapProductMediaCacheEntityToMediaDbEntity(MediaDbEntity productMedia) {
        return ProductMediaCacheEntity.builder()
                .id(productMedia.getId())
                .product_id(productMedia.getProductId())
                .organisation_id(productMedia.getOrganisationId())
                .created_at(productMedia.getCreatedAt())
                .build();
    }


//    public GetSpecMediaDTO getAllSpecificMedia(List<ProductMediaCacheEntity> reqBody, String message) {
//        return GetSpecMediaDTO.builder()
//                .status(true)
//                .message(message)
//                .product_id(reqBody.get(0).getProduct_id())
//                .media_type(reqBody.get(0).getMedia_type())
//                .is_active(reqBody.get(0).getIsActive())
//                .product_media(reqBody.stream().map(spec -> GetSpecMediaDTO.Media
//                        .builder()
//                        .id(spec.getId())
//                        .media_url(spec.getMedia_url())
//                        .is_primary(spec.getIs_primary())
//                        .updated_at(spec.getUpdated_at())
//                        .created_at(spec.getCreated_at())
//                        .build()).collect(Collectors.toList())
//                )
//                .build();
//    }


    //here is for media mapper.
//    public MediaDbEntity toUpdateProductMedia(
//            MediaDbEntity mediaData,
//            MediaUploadResponse mediaUploadResponse,
//            boolean isPrimary
//    ) {
//        return MediaDbEntity.builder()
//                .id(mediaData.getId())
//                .productId(mediaData.getProductId())
//                .organisationId(mediaData.getOrganisationId())
//                .mediaType(mediaUploadResponse.getMediaType())
//                .mediaUrl(mediaUploadResponse.getFileName())
//                .isPrimary(isPrimary)
//                .isActive(mediaData.getIsActive())
//                .updatedAt(LocalDateTime.now())
//                .createdAt(mediaData.getCreatedAt())
//                .build();
//    }
//
//    public MediaDbEntity productMediaBuilder(MediaDbEntity productMedia, String media) {
//        return MediaDbEntity.builder()
//                .id(productMedia.getId())
//                .productId(productMedia.getProductId())
//                .organisationId(productMedia.getOrganisationId())
//                .mediaType(productMedia.getMediaType())
//                .mediaUrl(media)
//                .isPrimary(productMedia.getIsPrimary())
//                .isActive(productMedia.getIsActive())
//                .updatedAt(LocalDateTime.now())
//                .createdAt(productMedia.getCreatedAt())
//                .build();
//    }
//
//    public MediaDbEntity primaryProductBuilder(MediaDbEntity productMedia, boolean primary) {
//        return MediaDbEntity.builder()
//                .id(productMedia.getId())
//                .productId(productMedia.getProductId())
//                .organisationId(productMedia.getOrganisationId())
//                .mediaType(productMedia.getMediaType())
//                .mediaUrl(productMedia.getMediaUrl())
//                .isPrimary(primary)
//                .isActive(productMedia.getIsActive())
//                .updatedAt(LocalDateTime.now())
//                .createdAt(productMedia.getCreatedAt())
//                .build();
//    }
//
//
//
//
//    public List<GetAllMediaModel.ImageMedia> filterAndMapMediaImage(List<MediaDbEntity> mediaList, String mediaType) {
//        return mediaList.stream()
//                .filter(media -> mediaType.equalsIgnoreCase(media.getMediaType()))
//                .map(media -> GetAllMediaModel.ImageMedia.builder()
//                        .id(media.getId())
//                        .is_primary(media.getIsPrimary())
//                        .media_url(media.getMediaUrl())
//                        .updated_at(media.getUpdatedAt())
//                        .created_at(media.getCreatedAt())
//                        .build()
//                ).collect(Collectors.toList());
//    }
//
//    public List<GetAllMediaModel.VideoMedia> filterAndMapMediaVideo(List<MediaDbEntity> mediaList, String mediaType) {
//        return mediaList.stream()
//                .filter(media -> mediaType.equalsIgnoreCase(media.getMediaType()))
//                .map(media -> GetAllMediaModel.VideoMedia.builder()
//                        .id(media.getId())
//                        .is_primary(media.getIsPrimary())
//                        .media_url(media.getMediaUrl())
//                        .updated_at(media.getUpdatedAt())
//                        .created_at(media.getCreatedAt())
//                        .build()
//                ).collect(Collectors.toList());
//    }
//
//    public List<GetAllMediaModel.ImageMedia> filterAndMapCacheMediaImage(List<ProductMediaCacheEntity> mediaList, String mediaType) {
//        return mediaList.stream()
//                .filter(media -> mediaType.equalsIgnoreCase(media.getMedia_type()))
//                .map(media -> GetAllMediaModel.ImageMedia.builder()
//                        .id(media.getId())
//                        .is_primary(media.getIs_primary())
//                        .media_url(media.getMedia_url())
//                        .updated_at(media.getUpdated_at())
//                        .created_at(media.getCreated_at())
//                        .build()
//                ).collect(Collectors.toList());
//    }
//
//    public List<GetAllMediaModel.VideoMedia> filterAndMapCacheMediaVideo(List<ProductMediaCacheEntity> mediaList, String mediaType) {
//        return mediaList.stream()
//                .filter(media -> mediaType.equalsIgnoreCase(media.getMedia_type()))
//                .map(media -> GetAllMediaModel.VideoMedia.builder()
//                        .id(media.getId())
//                        .is_primary(media.getIs_primary())
//                        .media_url(media.getMedia_url())
//                        .updated_at(media.getUpdated_at())
//                        .created_at(media.getCreated_at())
//                        .build()
//                ).collect(Collectors.toList());
//    }
//
//    public List<ProductMediaCacheEntity> mapCacheProductMedia(List<MediaDbEntity> productMedia) {
//        return productMedia.stream()
//                .map(media -> ProductMediaCacheEntity.builder()
//                        .id(media.getId())
//                        .product_id(media.getProductId())
//                        .organisation_id(media.getOrganisationId())
//                        .media_type(media.getMediaType())
//                        .media_url(media.getMediaUrl())
//                        .is_primary(media.getIsPrimary())
//                        .isActive(media.getIsActive())
//                        .updated_at(media.getUpdatedAt())
//                        .created_at(media.getCreatedAt())
//                        .build()
//                ).collect(Collectors.toList());
//    }
//
//    public List<GetSpecMediaDTO.Media> filterAndMapMedia(List<MediaDbEntity> mediaList, String mediaType) {
//        return mediaList.stream()
//                .filter(media -> mediaType.equalsIgnoreCase(media.getMediaType()))
//                .map(media -> GetSpecMediaDTO.Media.builder()
//                        .id(media.getId())
//                        .is_primary(media.getIsPrimary())
//                        .media_url(media.getMediaUrl())
//                        .updated_at(media.getUpdatedAt())
//                        .created_at(media.getCreatedAt())
//                        .build()
//                ).collect(Collectors.toList());
//    }
//
//
//
//    public MediaDbEntity mapSingleProductMediaToCache(ProductMediaCacheEntity media) {
//        return MediaDbEntity.builder()
//                .id(media.getId())
//                .isPrimary(media.getIs_primary())
//                .productId(media.getProduct_id())
//                .mediaType(media.getMedia_type())
//                .isActive(media.getIsActive())
//                .organisationId(media.getOrganisation_id())
//                .mediaUrl(media.getMedia_url())
//                .updatedAt(media.getUpdated_at())
//                .createdAt(media.getCreated_at())
//                .build();
//    }
//
//    public GetIndividualProductMediaResDTO.ProductMedia filterAndMapSingleProductMedia(MediaDbEntity mediaList) {
//        return GetIndividualProductMediaResDTO.ProductMedia
//                .builder()
//                .id(mediaList.getId())
//                .product_id(mediaList.getProductId())
//                .media_type(mediaList.getMediaType())
//                .is_primary(mediaList.getIsPrimary())
//                .media_url(mediaList.getMediaUrl())
//                .is_active(mediaList.getIsActive())
//                .updated_at(mediaList.getUpdatedAt())
//                .created_at(mediaList.getCreatedAt())
//                .build();
//    }

    //product Specification
    public ProductSpecificationDbModel mapProductSpec(AddProductSpecReqModel reqBody) {
        return ProductSpecificationDbModel.builder()
                .id(reqBody.getId())
                .productId(reqBody.getProduct_id())
                .organisationId(reqBody.getOrganisation_id())
                .length(Double.parseDouble(reqBody.getDimensions().getLength()))
                .width(Double.parseDouble(reqBody.getDimensions().getWidth()))
                .height(Double.parseDouble(reqBody.getDimensions().getHeight()))
                .weight(Double.parseDouble(reqBody.getWeight()))
                .materialDescription(reqBody.getMaterial_description())
                .certificationDescription(reqBody.getCertification_description())
                .isActive(reqBody.is_active())
                .updatedAt(reqBody.getUpdated_at())
                .createdAt(reqBody.getCreated_at())
                .build();
    }

    public ProductSpecificationCacheModel mapProductSpecToCache(ProductSpecificationDbModel reqBody) {
        return ProductSpecificationCacheModel.builder()
                .id(reqBody.getId())
                .productId(reqBody.getProductId())
                .organisationId(reqBody.getOrganisationId())
                .length(reqBody.getLength())
                .width(reqBody.getWidth())
                .height(reqBody.getHeight())
                .weight(reqBody.getWeight())
                .material_description(reqBody.getMaterialDescription())
                .certification_description(reqBody.getCertificationDescription())
                .is_active(reqBody.isActive())
                .updated_at(reqBody.getUpdatedAt())
                .created_at(reqBody.getCreatedAt())
                .build();
    }

    public ProductSpecificationDbModel mapCacheToProductSpec(ProductSpecificationCacheModel reqBody) {
        return ProductSpecificationDbModel.builder()
                .id(reqBody.getId())
                .productId(reqBody.getProductId())
                .organisationId(reqBody.getOrganisationId())
                .length(reqBody.getLength())
                .width(reqBody.getWidth())
                .height(reqBody.getHeight())
                .weight(reqBody.getWeight())
                .materialDescription(reqBody.getMaterial_description())
                .certificationDescription(reqBody.getCertification_description())
                .isActive(reqBody.is_active())
                .updatedAt(reqBody.getUpdated_at())
                .createdAt(reqBody.getCreated_at())
                .build();
    }

    public ProductSpecificationDbModel mapProductSpecToProductSpec(ProductSpecificationDbModel reqBody) {
        return ProductSpecificationDbModel.builder()
                .id(reqBody.getId())
                .productId(reqBody.getProductId())
                .organisationId(reqBody.getOrganisationId())
                .length(reqBody.getLength())
                .width(reqBody.getWidth())
                .height(reqBody.getHeight())
                .weight(reqBody.getWeight())
                .materialDescription(reqBody.getMaterialDescription())
                .certificationDescription(reqBody.getCertificationDescription())
                .isActive(reqBody.isActive())
                .updatedAt(reqBody.getUpdatedAt())
                .createdAt(reqBody.getCreatedAt())
                .build();
    }

    public AddProductSpecResModel productsSpecResponse(ProductSpecificationDbModel reqBody, String responseMessage) {
        return AddProductSpecResModel
                .builder()
                .status(true)
                .message(responseMessage)
                .id(reqBody.getId())
                .product_id(reqBody.getProductId())
                .weight(reqBody.getWeight())
                .dimensions(
                        AddProductSpecResModel.Dimensions
                                .builder()
                                .length(reqBody.getLength())
                                .width(reqBody.getWidth())
                                .height(reqBody.getHeight())
                                .build()
                )
                .material_description(reqBody.getMaterialDescription())
                .certification_description(reqBody.getCertificationDescription())
                .updated_at(reqBody.getUpdatedAt())
                .created_at(reqBody.getCreatedAt())
                .build();
    }

    public FetchAllProductSpecResModel productsSpecFetchAllResponse(List<ProductSpecificationDbModel> reqBody) {
        return FetchAllProductSpecResModel.builder()
                .status(true)
                .message("product specification successfully fetch")
                .products_specifications(reqBody.stream().map(spec -> FetchAllProductSpecResModel.ProductsSpecifications
                        .builder()
                        .id(spec.getId())
                        .product_id(spec.getProductId())
                        .weight(spec.getWeight())
                        .dimensions(FetchAllProductSpecResModel.ProductsSpecifications.Dimensions
                                .builder()
                                .length(spec.getLength())
                                .width(spec.getWidth())
                                .height(spec.getHeight())
                                .build())
                        .material_description(spec.getMaterialDescription())
                        .certification_description(spec.getCertificationDescription())
                        .updated_at(spec.getUpdatedAt())
                        .created_at(spec.getCreatedAt())
                        .build()).collect(Collectors.toList())
                )
                .build();
    }

    public List<ProductSpecificationDbModel> mapAllCacheToProductSpec(List<ProductSpecificationCacheModel> reqBody) {
        return reqBody.stream().map(spec -> ProductSpecificationDbModel.builder()
                .id(spec.getId())
                .productId(spec.getProductId())
                .organisationId(spec.getOrganisationId())
                .length(spec.getLength())
                .width(spec.getWidth())
                .height(spec.getHeight())
                .weight(spec.getWeight())
                .materialDescription(spec.getMaterial_description())
                .certificationDescription(spec.getCertification_description())
                .isActive(spec.is_active())
                .updatedAt(spec.getUpdated_at())
                .createdAt(spec.getCreated_at())
                .build()
        ).collect(Collectors.toList());
    }

    //shipping details start from here
    public ShippingDetailsDbModel mapAddShippingDetailsReqModelToDbModel(AddShippingDetailsReqModel reqModel) {
        return ShippingDetailsDbModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProduct_id())
                .organisationId(reqModel.getOrganisation_id())
                .shippingMethod(reqModel.getShipping_method())
                .shippingCost(reqModel.getShipping_cost())
                .estimatedDeliveryTime(reqModel.getEstimated_delivery_time())
                .countryCode(reqModel.getCountry_code())
                .region(reqModel.getRegion())
                .customsFees(reqModel.getCustoms_fees())
                .handlingTime(reqModel.getHandling_time())
                .crossBorder(reqModel.getCross_border())
                .isActive(reqModel.is_active())
                .updatedAt(reqModel.getUpdated_at())
                .createdAt(reqModel.getCreated_at())
                .build();
    }

    public ShippingDetailsCacheModel mapShippingDetailsCacheModelToDbModel(ShippingDetailsDbModel reqModel) {
        return ShippingDetailsCacheModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .shippingMethod(reqModel.getShippingMethod())
                .shippingCost(reqModel.getShippingCost())
                .estimatedDeliveryTime(reqModel.getEstimatedDeliveryTime())
                .countryCode(reqModel.getCountryCode())
                .region(reqModel.getRegion())
                .customsFees(reqModel.getCustomsFees())
                .handlingTime(reqModel.getHandlingTime())
                .crossBorder(reqModel.getCrossBorder())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build();
    }

    public ShippingDetailsDbModel mapCacheModelToDbModel(ShippingDetailsCacheModel reqModel) {
        return ShippingDetailsDbModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .shippingMethod(reqModel.getShippingMethod())
                .shippingCost(reqModel.getShippingCost())
                .estimatedDeliveryTime(reqModel.getEstimatedDeliveryTime())
                .countryCode(reqModel.getCountryCode())
                .region(reqModel.getRegion())
                .customsFees(reqModel.getCustomsFees())
                .handlingTime(reqModel.getHandlingTime())
                .crossBorder(reqModel.getCrossBorder())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build();
    }

//    public ShippingDetailsOneResModel shippingDetailsOneResponseBuilder(ShippingDetailsDbModel reqBody, String message) {
//        return ShippingDetailsOneResModel.builder()
//                .status(true)
//                .message(message)
//                .shipping_details(
//                        ShippingDetailsOneResModel.ShippingDetails
//                                .builder()
//                                .id(reqBody.getId())
//                                .product_id(reqBody.getProductId())
//                                .organisation_id(reqBody.getOrganisationId())
//                                .shipping_method(reqBody.getShippingMethod())
//                                .estimated_delivery_time(reqBody.getEstimatedDeliveryTime())
//                                .country_code(reqBody.getCountryCode())
//                                .region(reqBody.getRegion())
//                                .customs_fees(reqBody.getCustomsFees())
//                                .handling_time(reqBody.getHandlingTime())
//                                .cross_border(reqBody.getCrossBorder())
//                                .is_active(reqBody.isActive())
//                                .updated_at(reqBody.getUpdatedAt())
//                                .created_at(reqBody.getCreatedAt())
//                                .build()
//                )
//                .build();
//    }

    public List<ShippingDetailsDbModel> mapAllCacheToDbModel(List<ShippingDetailsCacheModel> reqBody) {
        return reqBody.stream().map(spec -> ShippingDetailsDbModel.builder()
                .id(spec.getId())
                .productId(spec.getProductId())
                .organisationId(spec.getOrganisationId())
                .shippingMethod(spec.getShippingMethod())
                .shippingCost(spec.getShippingCost())
                .estimatedDeliveryTime(spec.getEstimatedDeliveryTime())
                .countryCode(spec.getCountryCode())
                .region(spec.getRegion())
                .customsFees(spec.getCustomsFees())
                .handlingTime(spec.getHandlingTime())
                .crossBorder(spec.getCrossBorder())
                .isActive(spec.isActive())
                .updatedAt(spec.getUpdatedAt())
                .createdAt(spec.getCreatedAt())
                .build()
        ).collect(Collectors.toList());
    }

    public ShippingDetailsAllResModel shippingDetailsAllResponseBuilder(List<ShippingDetailsDbModel> reqBody, String message) {
        return ShippingDetailsAllResModel.builder()
                .status(true)
                .message(message)
                .shipping_details(
                        reqBody.stream().map(ship -> ShippingDetailsAllResModel.ShippingDetails
                                .builder()
                                .id(ship.getId())
                                .product_id(ship.getProductId())
                                .organisation_id(ship.getOrganisationId())
                                .shipping_method(ship.getShippingMethod())
                                .estimated_delivery_time(ship.getEstimatedDeliveryTime())
                                .country_code(ship.getCountryCode())
                                .region(ship.getRegion())
                                .customs_fees(ship.getCustomsFees())
                                .handling_time(ship.getHandlingTime())
                                .cross_border(ship.getCrossBorder())
                                .is_active(ship.isActive())
                                .updated_at(ship.getUpdatedAt())
                                .created_at(ship.getCreatedAt())
                                .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }

    //product policies
    public ProductPolicyResDTO productPolicyOneResponseBuilder(ProductPolicyDbEntity reqBody, String message) {
        return ProductPolicyResDTO.builder()
                .status(true)
                .message(message)
                .product_policies(
                        ProductPolicyResDTO.ProductPolicy
                                .builder()
                                .id(reqBody.getId())
                                .product_id(reqBody.getProductId())
                                .organisation_id(reqBody.getOrganisationId())
                                .return_policy_description(reqBody.getReturnPolicyDescription())
                                .warranty_period(reqBody.getWarrantyPeriod())
                                .return_policy_description(reqBody.getReturnPolicyDescription())
                                .is_active(reqBody.isActive())
                                .updated_at(reqBody.getUpdatedAt())
                                .created_at(reqBody.getCreatedAt())
                                .build()
                )
                .build();
    }

    public ProductPolicyDbEntity mapAddProductPolicyReqModelToDbModel(CreateProductPolicyReqDTO reqModel) {
        return ProductPolicyDbEntity.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProduct_id())
                .organisationId(reqModel.getOrganisation_id())
                .warrantyDescription(reqModel.getWarranty_description())
                .warrantyPeriod(reqModel.getWarranty_period())
                .returnPolicyDescription(reqModel.getReturn_policy_description())
                .isActive(reqModel.is_active())
                .updatedAt(reqModel.getUpdated_at())
                .createdAt(reqModel.getCreated_at())
                .build();
    }

    public ProductPolicyCacheEntity mapProductPolicyToCache(ProductPolicyDbEntity reqBody) {
        return ProductPolicyCacheEntity.builder()
                .id(reqBody.getId())
                .productId(reqBody.getProductId())
                .organisationId(reqBody.getOrganisationId())
                .warrantyDescription(reqBody.getWarrantyDescription())
                .warrantyPeriod(reqBody.getWarrantyPeriod())
                .returnPolicyDescription(reqBody.getReturnPolicyDescription())
                .isActive(reqBody.isActive())
                .updatedAt(reqBody.getUpdatedAt())
                .createdAt(reqBody.getCreatedAt())
                .build();
    }

    public ProductPolicyDbEntity mapCacheToProductPolicy(ProductPolicyCacheEntity reqBody) {
        return ProductPolicyDbEntity.builder()
                .id(reqBody.getId())
                .productId(reqBody.getProductId())
                .organisationId(reqBody.getOrganisationId())
                .warrantyDescription(reqBody.getWarrantyDescription())
                .warrantyPeriod(reqBody.getWarrantyPeriod())
                .returnPolicyDescription(reqBody.getReturnPolicyDescription())
                .isActive(reqBody.isActive())
                .updatedAt(reqBody.getUpdatedAt())
                .createdAt(reqBody.getCreatedAt())
                .build();
    }

    public ProductPolicyAllResModel productPolicyResponseBuilder(List<ProductPolicyDbEntity> reqBody) {
        return ProductPolicyAllResModel.builder()
                .status(true)
                .message("")
                .product_policies(
                        reqBody.stream().map(policy -> ProductPolicyAllResModel.ProductPolicy
                                .builder()
                                .id(policy.getId())
                                .product_id(policy.getProductId())
                                .organisation_id(policy.getOrganisationId())
                                .warranty_description(policy.getWarrantyDescription())
                                .warranty_period(policy.getWarrantyPeriod())
                                .return_policy_description(policy.getReturnPolicyDescription())
                                .is_active(policy.isActive())
                                .updated_at(policy.getUpdatedAt())
                                .created_at(policy.getCreatedAt())
                                .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }

    public List<ProductPolicyDbEntity> mapAllCacheToProductPolicy(List<ProductPolicyCacheEntity> reqBody) {
        return reqBody.stream().map(spec -> ProductPolicyDbEntity.builder()
                .id(spec.getId())
                .productId(spec.getProductId())
                .organisationId(spec.getOrganisationId())
                .warrantyDescription(spec.getWarrantyDescription())
                .warrantyPeriod(spec.getWarrantyPeriod())
                .returnPolicyDescription(spec.getReturnPolicyDescription())
                .isActive(spec.isActive())
                .updatedAt(spec.getUpdatedAt())
                .createdAt(spec.getCreatedAt())
                .build()
        ).collect(Collectors.toList());
    }


    //Product_Review
    public ProductReviewDbModel mapAddProductReviewReqModelToDbModel(AddProductReviewReqModel reqModel) {
        return ProductReviewDbModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProduct_id())
                .organisationId(reqModel.getOrganisation_id())
                .userId(utilitiesManager.convertStringToUUID(reqModel.getUser_id()))
                .rating(reqModel.getRating())
                .reviewText(reqModel.getReview_text())
                .isActive(reqModel.is_active())
                .updatedAt(reqModel.getUpdated_at())
                .createdAt(reqModel.getCreated_at())
                .build();
    }

    public ProductReviewCacheModel mapProductReviewCacheModelToDbModel(ProductReviewDbModel reqModel) {
        return ProductReviewCacheModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .userId(reqModel.getUserId())
                .rating(reqModel.getRating())
                .reviewText(reqModel.getReviewText())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build();
    }

    public ProductReviewOneResModel productReviewOneResponseBuilder(ProductReviewDbModel reqModel, String message) {
        return ProductReviewOneResModel.builder()
                .status(true)
                .message(message)
                .product_reviews(
                        ProductReviewOneResModel.ProductReview
                                .builder()
                                .id(reqModel.getId())
                                .product_id(reqModel.getProductId())
                                .organisation_id(reqModel.getOrganisationId())
                                .user_id(reqModel.getUserId().toString())
                                .rating(reqModel.getRating())
                                .review_text(reqModel.getReviewText())
                                .is_active(reqModel.isActive())
                                .updated_at(reqModel.getUpdatedAt())
                                .created_at(reqModel.getCreatedAt())
                                .build()
                )
                .build();
    }

    public ProductReviewDbModel mapDbModelToProductReviewCacheModel(ProductReviewCacheModel reqModel) {
        return ProductReviewDbModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .userId(reqModel.getUserId())
                .rating(reqModel.getRating())
                .reviewText(reqModel.getReviewText())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build();
    }

    public List<ProductReviewDbModel> mapToAllProductReview(List<ProductReviewCacheModel> spec) {
        return spec.stream().map(reqModel -> ProductReviewDbModel.builder()
                .id(reqModel.getId())
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .userId(reqModel.getUserId())
                .rating(reqModel.getRating())
                .reviewText(reqModel.getReviewText())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build()
        ).collect(Collectors.toList());
    }

//    public ProductReviewAllResModel allProductReviewResponseBuilder(List<ProductReviewDbModel> reqBody, String message) {
//        return ProductReviewAllResModel.builder()
//                .status(true)
//                .message(message)
//                .product_reviews(
//                        reqBody.stream().map(review -> ProductReviewAllResModel.ProductReview
//                                .builder()
//                                .id(review.getId())
//                                .id(review.getId())
//                                .product_id(review.getProductId())
//                                .organisation_id(review.getOrganisationId())
//                                .user_id(review.getUserId().toString())
//                                .rating(review.getRating())
//                                .review_text(review.getReviewText())
//                                .is_active(review.isActive())
//                                .updated_at(review.getUpdatedAt())
//                                .created_at(review.getCreatedAt())
//                                .build()
//                        ).collect(Collectors.toList())
//                )
//                .build();
//    }


    //Related_Product
    public RelatedProductsDbModel mapAddRelatedProductModelToDbModel(AddRelatedProductsReqModel reqModel) {
        return RelatedProductsDbModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProduct_id())
                .organisationId(reqModel.getOrganisation_id())
                .relatedProductId(reqModel.getRelated_product_id())
                .isActive(reqModel.is_active())
                .updatedAt(reqModel.getUpdated_at())
                .createdAt(reqModel.getCreated_at())
                .build();
    }

    public RelatedProductsCacheModel mapRelatedProductsCacheModelToDbModel(RelatedProductsDbModel reqModel) {
        return RelatedProductsCacheModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .relatedProductId(reqModel.getRelatedProductId())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build();
    }

    public RelatedProductsOneResModel relatedProductsOneResponseBuilder(RelatedProductsDbModel reqModel, String message) {
        return RelatedProductsOneResModel.builder()
                .status(true)
                .message(message)
                .related_products(
                        RelatedProductsOneResModel.RelatedProduct
                                .builder()
                                .id(reqModel.getId())
                                .product_id(reqModel.getProductId())
                                .organisation_id(reqModel.getOrganisationId())
                                .related_product_id(reqModel.getRelatedProductId())
                                .is_active(reqModel.isActive())
                                .updated_at(reqModel.getUpdatedAt())
                                .created_at(reqModel.getCreatedAt())
                                .build()
                )
                .build();
    }

    public RelatedProductsDbModel mapDbModelToRelatedProductsCacheModel(RelatedProductsCacheModel reqModel) {
        return RelatedProductsDbModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .relatedProductId(reqModel.getRelatedProductId())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build();
    }

    public List<RelatedProductsDbModel> mapToAllRelatedProduct(List<RelatedProductsCacheModel> spec) {
        return spec.stream().map(reqModel -> RelatedProductsDbModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .relatedProductId(reqModel.getRelatedProductId())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build()
        ).collect(Collectors.toList());
    }

    public RelatedProductsAllResModel allRelatedProductResponseBuilder(List<RelatedProductsDbModel> reqBody, String message) {
        return RelatedProductsAllResModel.builder()
                .status(true)
                .message(message)
                .related_products(
                        reqBody.stream().map(review -> RelatedProductsAllResModel.RelatedProduct
                                .builder()
                                .id(review.getId())
                                .product_id(review.getProductId())
                                .organisation_id(review.getOrganisationId())
                                .related_product_id(review.getRelatedProductId())
                                .is_active(review.isActive())
                                .updated_at(review.getUpdatedAt())
                                .created_at(review.getCreatedAt())
                                .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }


    //Special_Offer
    public SpecialOffersDbModel mapAddSpecialOfferModelToDbModel(AddSpecialOffersReqModel reqModel) {
        return SpecialOffersDbModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProduct_id())
                .organisationId(reqModel.getOrganisation_id())
                .offerDescription(reqModel.getOffer_description())
                .discountPercentage(reqModel.getDiscount_percentage())
                .startDate(reqModel.getStart_date())
                .endDate(reqModel.getEnd_date())
                .isActive(reqModel.is_active())
                .updatedAt(reqModel.getUpdated_at())
                .createdAt(reqModel.getCreated_at())
                .build();
    }

    public SpecialOffersCacheModel mapSpecialOffersDbModelToDbModel(SpecialOffersDbModel reqModel) {
        return SpecialOffersCacheModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .offerDescription(reqModel.getOfferDescription())
                .discountPercentage(reqModel.getDiscountPercentage())
                .startDate(reqModel.getStartDate())
                .endDate(reqModel.getEndDate())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build();
    }

    public SpecialOffersOneResModel specialOffersOneResponseBuilder(SpecialOffersDbModel reqModel, String message) {
        return SpecialOffersOneResModel.builder()
                .status(true)
                .message(message)
                .special_offers(
                        SpecialOffersOneResModel.SpecialOffers
                                .builder()
                                .id(reqModel.getId())
                                .product_id(reqModel.getProductId())
                                .organisation_id(reqModel.getOrganisationId())
                                .offer_description(reqModel.getOfferDescription())
                                .discount_percentage(reqModel.getDiscountPercentage())
                                .start_date(reqModel.getStartDate())
                                .end_date(reqModel.getEndDate())
                                .is_active(reqModel.isActive())
                                .updated_at(reqModel.getUpdatedAt())
                                .created_at(reqModel.getCreatedAt())
                                .build()
                )
                .build();
    }

    public SpecialOffersDbModel mapDbModelToSpecialOffersDbModel(SpecialOffersCacheModel reqModel) {
        return SpecialOffersDbModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .offerDescription(reqModel.getOfferDescription())
                .discountPercentage(reqModel.getDiscountPercentage())
                .startDate(reqModel.getStartDate())
                .endDate(reqModel.getEndDate())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build();
    }

    public List<SpecialOffersDbModel> mapToAllSpecialOffer(List<SpecialOffersCacheModel> spec) {
        return spec.stream().map(reqModel -> SpecialOffersDbModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .offerDescription(reqModel.getOfferDescription())
                .discountPercentage(reqModel.getDiscountPercentage())
                .startDate(reqModel.getStartDate())
                .endDate(reqModel.getEndDate())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build()
        ).collect(Collectors.toList());
    }

    public SpecialOffersAllResModel allSpecialOfferResponseBuilder(List<SpecialOffersDbModel> reqBody, String message) {
        return SpecialOffersAllResModel.builder()
                .status(true)
                .message(message)
                .special_offers(
                        reqBody.stream().map(review -> SpecialOffersAllResModel.SpecialOffers
                                .builder()
                                .id(review.getId())
                                .product_id(review.getProductId())
                                .organisation_id(review.getOrganisationId())
                                .offer_description(review.getOfferDescription())
                                .discount_percentage(review.getDiscountPercentage())
                                .start_date(review.getStartDate())
                                .end_date(review.getEndDate())
                                .is_active(review.isActive())
                                .updated_at(review.getUpdatedAt())
                                .created_at(review.getCreatedAt())
                                .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }


    //Product Tags
    public ProductTagDbModel mapAddProductTagModelToDbModel(AddProductTagReqModel reqModel) {
        return ProductTagDbModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProduct_id())
                .organisationId(reqModel.getOrganisation_id())
                .tag(reqModel.getTag())
                .isActive(reqModel.is_active())
                .updatedAt(reqModel.getUpdated_at())
                .createdAt(reqModel.getCreated_at())
                .build();
    }

    public ProductTagCacheModel mapProductTagDbModelToDbModel(ProductTagDbModel reqModel) {
        return ProductTagCacheModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .tag(reqModel.getTag())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build();
    }

    public ProductTagOneResModel productTagResponseBuilder(ProductTagDbModel reqModel, String message) {
        return ProductTagOneResModel.builder()
                .status(true)
                .message(message)
                .product_tags(
                        ProductTagOneResModel.ProductTag
                                .builder()
                                .id(reqModel.getId())
                                .product_id(reqModel.getProductId())
                                .organisation_id(reqModel.getOrganisationId())
                                .tag(reqModel.getTag())
                                .is_active(reqModel.isActive())
                                .updated_at(reqModel.getUpdatedAt())
                                .created_at(reqModel.getCreatedAt())
                                .build()
                )
                .build();
    }

    public ProductTagDbModel mapDbModelToProductTagDbModel(ProductTagCacheModel reqModel) {
        return ProductTagDbModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .tag(reqModel.getTag())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build();
    }

    public List<ProductTagDbModel> mapToAllProductTag(List<ProductTagCacheModel> spec) {
        return spec.stream().map(reqModel -> ProductTagDbModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProductId())
                .organisationId(reqModel.getOrganisationId())
                .tag(reqModel.getTag())
                .isActive(reqModel.isActive())
                .updatedAt(reqModel.getUpdatedAt())
                .createdAt(reqModel.getCreatedAt())
                .build()
        ).collect(Collectors.toList());
    }


}
