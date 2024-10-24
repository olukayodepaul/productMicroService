package com.dart.product.mapper;

import com.dart.product.entity.product_media_model.*;
import com.dart.product.entity.product_model.*;
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
        return productMedia.stream().map(media-> MediaDbModel
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
}