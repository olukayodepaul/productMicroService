package com.dart.product.mapper;

import com.dart.product.entity.product_media_model.MediaDbModel;
import com.dart.product.entity.product_media_model.ProductMediaCacheModel;
import com.dart.product.entity.product_media_model.ProductMediaResModel;
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

}


