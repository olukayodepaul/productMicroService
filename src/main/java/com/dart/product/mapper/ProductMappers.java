package com.dart.product.mapper;

import com.dart.product.entity.product_comments_model.AddProductCommentReqModel;
import com.dart.product.entity.product_comments_model.ProductCommentDbModel;
import com.dart.product.entity.product_feedback.AddProductFeedBackReqModel;
import com.dart.product.entity.product_feedback.ProductFeedBackDbModel;
import com.dart.product.entity.product_media_model.*;
import com.dart.product.entity.product_model.*;
import com.dart.product.entity.product_policy_model.*;
import com.dart.product.entity.product_reviews_model.AddProductReviewReqModel;
import com.dart.product.entity.product_reviews_model.ProductReviewCacheModel;
import com.dart.product.entity.product_reviews_model.ProductReviewDbModel;
import com.dart.product.entity.product_specification_model.*;
import com.dart.product.entity.product_tags_model.AddProductTagReqModel;
import com.dart.product.entity.product_tags_model.ProductTagDbModel;
import com.dart.product.entity.related_products_model.AddRelatedProductsReqModel;
import com.dart.product.entity.related_products_model.RelatedProductsDbModel;
import com.dart.product.entity.shipping_details_model.*;
import com.dart.product.entity.special_offers_model.AddSpecialOffersReqModel;
import com.dart.product.entity.special_offers_model.SpecialOffersDbModel;
import com.dart.product.utilities.UtilitiesManager;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductMappers {

    private final UtilitiesManager utilitiesManager;

    public ProductMappers(UtilitiesManager utilitiesManager) {
        this.utilitiesManager = utilitiesManager;
    }

    public ProductDbModel toProduct(ProductReqModel product) {
        return ProductDbModel.builder()
                .organisationId(product.getOrganisation_id())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .category_id(product.getCategory_id())
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .isActive(true)
                .build();
    }

    public ProductResModel.Product toProductResponse(ProductDbModel product) {
        return ProductResModel.Product.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .category_id(product.getCategory_id())
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .is_active(true)
                .build();
    }

    public ProductResModel.Product toProductCacheResponse(ProductCacheModel product) {
        return ProductResModel.Product.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .category_id(product.getCategory_id())
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .is_active(true)
                .build();
    }

    public ProductCacheModel toProductCache(ProductDbModel product) {
        return ProductCacheModel.builder()
                .id(product.getId())
                .organisation_id(product.getOrganisationId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .category_id(product.getCategory_id())
                .is_active(product.getIsActive())
                .updated_at(product.getUpdated_at())
                .created_at(product.getCreated_at())
                .build();
    }

    public ProductDbModel toProductBuilder(ProductDbModel product, ProductReqModel reqModel) {
        return ProductDbModel.builder()
                .id(product.getId())
                .organisationId(product.getOrganisationId())
                .name(reqModel.getName())
                .description(reqModel.getDescription())
                .price(reqModel.getPrice())
                .discount(reqModel.getDiscount())
                .category_id(reqModel.getCategory_id())
                .isActive(product.getIsActive())
                .updated_at(LocalDateTime.now())
                .created_at(product.getCreated_at())
                .build();
    }

    public List<AllProductResModel.Product> toCacheResponse(List<ProductCacheModel> product) {
        return product
                .stream()
                .map(products -> AllProductResModel.Product.builder()
                        .id(products.getId())
                        .name(products.getName())
                        .description(products.getDescription())
                        .price(products.getPrice())
                        .discount(products.getDiscount())
                        .category_id(products.getCategory_id())
                        .is_active(products.getIs_active())
                        .updated_at(products.getUpdated_at())
                        .created_at(products.getCreated_at())
                        .build()
                ).collect(Collectors.toList());
    }

    public List<ProductCacheModel> toCacheFromProduct(List<ProductDbModel> product) {
        return product
                .stream()
                .map(products -> ProductCacheModel.builder()
                        .id(products.getId())
                        .name(products.getName())
                        .description(products.getDescription())
                        .organisation_id(product.getLast().getOrganisationId())
                        .price(products.getPrice())
                        .discount(products.getDiscount())
                        .category_id(products.getCategory_id())
                        .is_active(products.getIsActive())
                        .updated_at(products.getUpdated_at())
                        .created_at(products.getCreated_at())
                        .build()
                ).collect(Collectors.toList());
    }


    //here is for media mapper.
    public MediaDbModel toProductMedia(
            Integer productId,
            UUID organisationId,
            String mediaType,
            String mediaUrl,
            Boolean isPrimary
    ) {
        return MediaDbModel.builder()
                .productId(productId)
                .organisationId(organisationId)
                .mediaType(mediaType)
                .mediaUrl(mediaUrl)
                .isPrimary(isPrimary)
                .isActive(true)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public MediaDbModel toUpdateProductMedia(
            MediaDbModel mediaData,
            MediaUploadResponse mediaUploadResponse,
            boolean isPrimary
    ) {
        return MediaDbModel.builder()
                .id(mediaData.getId())
                .productId(mediaData.getProductId())
                .organisationId(mediaData.getOrganisationId())
                .mediaType(mediaUploadResponse.getMediaType())
                .mediaUrl(mediaUploadResponse.getFileName())
                .isPrimary(isPrimary)
                .isActive(mediaData.getIsActive())
                .updatedAt(LocalDateTime.now())
                .createdAt(mediaData.getCreatedAt())
                .build();
    }

    public MediaDbModel productMediaBuilder(MediaDbModel productMedia, String media) {
        return MediaDbModel.builder()
                .id(productMedia.getId())
                .productId(productMedia.getProductId())
                .organisationId(productMedia.getOrganisationId())
                .mediaType(productMedia.getMediaType())
                .mediaUrl(media)
                .isPrimary(productMedia.getIsPrimary())
                .isActive(productMedia.getIsActive())
                .updatedAt(LocalDateTime.now())
                .createdAt(productMedia.getCreatedAt())
                .build();
    }

    public MediaDbModel primaryProductBuilder(MediaDbModel productMedia, boolean primary) {
        return MediaDbModel.builder()
                .id(productMedia.getId())
                .productId(productMedia.getProductId())
                .organisationId(productMedia.getOrganisationId())
                .mediaType(productMedia.getMediaType())
                .mediaUrl(productMedia.getMediaUrl())
                .isPrimary(primary)
                .isActive(productMedia.getIsActive())
                .updatedAt(LocalDateTime.now())
                .createdAt(productMedia.getCreatedAt())
                .build();
    }

    public ProductMediaCacheModel toCacheProductMedia(MediaDbModel productMedia) {
        return ProductMediaCacheModel.builder()
                .id(productMedia.getId())
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

    public ProductMediaResModel.ProductMedia toProductMediaResponse(MediaDbModel productMedia) {
        return ProductMediaResModel.ProductMedia.builder()
                .id(productMedia.getId())
                .product_id(productMedia.getProductId())
                .media_type(productMedia.getMediaType())
                .is_primary(productMedia.getIsPrimary())
                .isActive(productMedia.getIsActive())
                .media_url(productMedia.getMediaUrl())
                .updated_at(productMedia.getUpdatedAt())
                .created_at(productMedia.getCreatedAt())
                .build();
    }

    public List<GetAllMediaModel.ImageMedia> filterAndMapMediaImage(List<MediaDbModel> mediaList, String mediaType) {
        return mediaList.stream()
                .filter(media -> mediaType.equalsIgnoreCase(media.getMediaType()))
                .map(media -> GetAllMediaModel.ImageMedia.builder()
                        .id(media.getId())
                        .is_primary(media.getIsPrimary())
                        .media_url(media.getMediaUrl())
                        .updated_at(media.getUpdatedAt())
                        .created_at(media.getCreatedAt())
                        .build()
                ).collect(Collectors.toList());
    }

    public List<GetAllMediaModel.VideoMedia> filterAndMapMediaVideo(List<MediaDbModel> mediaList, String mediaType) {
        return mediaList.stream()
                .filter(media -> mediaType.equalsIgnoreCase(media.getMediaType()))
                .map(media -> GetAllMediaModel.VideoMedia.builder()
                        .id(media.getId())
                        .is_primary(media.getIsPrimary())
                        .media_url(media.getMediaUrl())
                        .updated_at(media.getUpdatedAt())
                        .created_at(media.getCreatedAt())
                        .build()
                ).collect(Collectors.toList());
    }

    public List<GetAllMediaModel.ImageMedia> filterAndMapCacheMediaImage(List<ProductMediaCacheModel> mediaList, String mediaType) {
        return mediaList.stream()
                .filter(media -> mediaType.equalsIgnoreCase(media.getMedia_type()))
                .map(media -> GetAllMediaModel.ImageMedia.builder()
                        .id(media.getId())
                        .is_primary(media.getIs_primary())
                        .media_url(media.getMedia_url())
                        .updated_at(media.getUpdated_at())
                        .created_at(media.getCreated_at())
                        .build()
                ).collect(Collectors.toList());
    }

    public List<GetAllMediaModel.VideoMedia> filterAndMapCacheMediaVideo(List<ProductMediaCacheModel> mediaList, String mediaType) {
        return mediaList.stream()
                .filter(media -> mediaType.equalsIgnoreCase(media.getMedia_type()))
                .map(media -> GetAllMediaModel.VideoMedia.builder()
                        .id(media.getId())
                        .is_primary(media.getIs_primary())
                        .media_url(media.getMedia_url())
                        .updated_at(media.getUpdated_at())
                        .created_at(media.getCreated_at())
                        .build()
                ).collect(Collectors.toList());
    }

    public List<ProductMediaCacheModel> mapCacheProductMedia(List<MediaDbModel> productMedia) {
        return productMedia.stream()
                .map(media -> ProductMediaCacheModel.builder()
                        .id(media.getId())
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

    public List<GetSpecMediaModel.Media> filterAndMapMedia(List<MediaDbModel> mediaList, String mediaType) {
        return mediaList.stream()
                .filter(media -> mediaType.equalsIgnoreCase(media.getMediaType()))
                .map(media -> GetSpecMediaModel.Media.builder()
                        .id(media.getId())
                        .is_primary(media.getIsPrimary())
                        .media_url(media.getMediaUrl())
                        .updated_at(media.getUpdatedAt())
                        .created_at(media.getCreatedAt())
                        .build()
                ).collect(Collectors.toList());
    }

    public List<MediaDbModel> mapProductMedia(List<ProductMediaCacheModel> productMedia) {
        return productMedia.stream().map(media -> MediaDbModel
                .builder()
                .id(media.getId())
                .isPrimary(media.getIs_primary())
                .productId(media.getProduct_id())
                .mediaType(media.getMedia_type())
                .isActive(media.getIsActive())
                .organisationId(media.getOrganisation_id())
                .mediaUrl(media.getMedia_url())
                .updatedAt(media.getUpdated_at())
                .createdAt(media.getCreated_at())
                .build()
        ).collect(Collectors.toList());
    }

    public MediaDbModel mapSingleProductMediaToCache(ProductMediaCacheModel media) {
        return MediaDbModel.builder()
                .id(media.getId())
                .isPrimary(media.getIs_primary())
                .productId(media.getProduct_id())
                .mediaType(media.getMedia_type())
                .isActive(media.getIsActive())
                .organisationId(media.getOrganisation_id())
                .mediaUrl(media.getMedia_url())
                .updatedAt(media.getUpdated_at())
                .createdAt(media.getCreated_at())
                .build();
    }

    public GetIndividualProductMediaModel.ProductMedia filterAndMapSingleProductMedia(MediaDbModel mediaList) {
        return GetIndividualProductMediaModel.ProductMedia
                .builder()
                .id(mediaList.getId())
                .product_id(mediaList.getProductId())
                .media_type(mediaList.getMediaType())
                .is_primary(mediaList.getIsPrimary())
                .media_url(mediaList.getMediaUrl())
                .is_active(mediaList.getIsActive())
                .updated_at(mediaList.getUpdatedAt())
                .created_at(mediaList.getCreatedAt())
                .build();
    }

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

    public ShippingDetailsOneResModel shippingDetailsOneResponseBuilder(ShippingDetailsDbModel reqBody, String message) {
        return ShippingDetailsOneResModel.builder()
                .status(true)
                .message(message)
                .shipping_details(
                        ShippingDetailsOneResModel.ShippingDetails
                                .builder()
                                .id(reqBody.getId())
                                .product_id(reqBody.getProductId())
                                .organisation_id(reqBody.getOrganisationId())
                                .shipping_method(reqBody.getShippingMethod())
                                .estimated_delivery_time(reqBody.getEstimatedDeliveryTime())
                                .country_code(reqBody.getCountryCode())
                                .region(reqBody.getRegion())
                                .customs_fees(reqBody.getCustomsFees())
                                .handling_time(reqBody.getHandlingTime())
                                .cross_border(reqBody.getCrossBorder())
                                .is_active(reqBody.isActive())
                                .updated_at(reqBody.getUpdatedAt())
                                .created_at(reqBody.getCreatedAt())
                                .build()
                )
                .build();
    }

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
    public ProductPolicyOneResModel productPolicyOneResponseBuilder(ProductPolicyDbModel reqBody, String message) {
        return ProductPolicyOneResModel.builder()
                .status(true)
                .message(message)
                .product_policies(
                        ProductPolicyOneResModel.ProductPolicy
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

    public ProductPolicyDbModel mapAddProductPolicyReqModelToDbModel(AddProductPolicyReqModel reqModel) {
        return ProductPolicyDbModel.builder()
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

    public ProductPolicyCacheModel mapProductPolicyToCache(ProductPolicyDbModel reqBody) {
        return ProductPolicyCacheModel.builder()
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

    public  ProductPolicyDbModel mapCacheToProductPolicy(ProductPolicyCacheModel reqBody) {
        return ProductPolicyDbModel.builder()
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

    public ProductPolicyAllResModel productPolicyResponseBuilder(List<ProductPolicyDbModel> reqBody) {
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

    public List<ProductPolicyDbModel> mapAllCacheToProductPolicy(List<ProductPolicyCacheModel> reqBody) {
        return reqBody.stream().map(spec -> ProductPolicyDbModel.builder()
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


    //Related_Product
    public RelatedProductsDbModel  mapAddRelatedProductModelToDbModel(AddRelatedProductsReqModel reqModel) {
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

    //Product Tags
    public ProductTagDbModel  mapAddProductTagModelToDbModel(AddProductTagReqModel reqModel) {
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


    //Product Comment
    public ProductCommentDbModel mapAddProductCommentModelToDbModel(AddProductCommentReqModel reqModel) {
        return ProductCommentDbModel.builder()
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

    //Product Feedback
    public ProductFeedBackDbModel mapAddProductFeedBackModelToDbModel(AddProductFeedBackReqModel reqModel) {
        return ProductFeedBackDbModel.builder()
                .id(reqModel.getId())
                .productId(reqModel.getProduct_id())
                .organisationId(reqModel.getOrganisation_id())
                .userId(utilitiesManager.convertStringToUUID(reqModel.getUser_id()))
                .feedbackType(reqModel.getFeedback_type())
                .isActive(reqModel.is_active())
                .updatedAt(reqModel.getUpdated_at())
                .createdAt(reqModel.getCreated_at())
                .build();
    }




}
