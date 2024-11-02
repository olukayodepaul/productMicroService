package com.dart.product.service.product;

import com.dart.product.dto_model.product_dto_model.AllProductResDto;
import com.dart.product.dto_model.product_dto_model.FetchAllProductsResModel;
import com.dart.product.entity.product_entity.ProductCacheEntity;
import com.dart.product.entity.product_entity.ProductDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductsRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAllProductService {

    private final ProductsRepo productsRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final ValidationUtils validationUtils;
    private final RedisProductCacheRepo redisProductCacheRepo;

    public ResponseEntity<AllProductResDto> getAllProduct(String authToken, int offset, int limit) {
        validateRequestToken(authToken);
        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));
        validateBruteForceProtection(userId.toString());

        limit = getValidLimit(limit);
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.ASC, "id"));
        FetchAllProductsResModel getAllCacheProduct = redisProductCacheRepo.getAllProducts(organisationId.toString());

        List<ProductDbEntity> products;
        AllProductResDto.PaginationMetadata pagination;

        if (getAllCacheProduct.getStatus()) {
            // Cache exists, apply pagination
            int totalProducts = getAllCacheProduct.getProduct().size();
            int start = Math.min(offset, totalProducts); // Correctly limit the start index
            int end = Math.min(start + limit, totalProducts); // Correctly limit the end index

            // Handle empty sublist case
            if (start >= totalProducts) {
                products = List.of(); // Return an empty list if the offset is out of bounds
            } else {
                List<ProductCacheEntity> paginatedProducts = getAllCacheProduct.getProduct().subList(start, end);
                products = productMappers.toCacheFromProduct(paginatedProducts, pageable).getContent();
            }

            pagination = buildPaginationMetadataFromCache(totalProducts, limit, offset);

        } else {
            // Fallback to fetching from the database if no cache exists
            Page<ProductDbEntity> productPage = findByOrganisationIdAndIsActive(organisationId, pageable);
            products = productPage.getContent();
            pagination = buildPaginationMetadataFromRepo(productPage);
        }

        List<AllProductResDto.Product> productList = products.stream()
                .map(productMappers::toProductDto)
                .collect(Collectors.toList());

        AllProductResDto response = AllProductResDto.builder()
                .status(true)
                .message(AppConfig.GET_PRODUCT_RESPONSE)
                .products(productList)
                .pagination(pagination)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private AllProductResDto.PaginationMetadata buildPaginationMetadataFromCache(int totalProducts, int limit, int offset) {
        int totalPages = (int) Math.ceil((double) totalProducts / limit); // Total pages calculation
        int currentPage = offset / limit; // 0-based current page

        Integer previousOffset = currentPage > 0 ? (currentPage - 1) * limit : null;
        Integer nextOffset = currentPage < totalPages - 1 ? (currentPage + 1) * limit : null;

        return AllProductResDto.PaginationMetadata.builder()
                .currentPage(currentPage + 1) // Convert to 1-based index
                .pageSize(limit)
                .totalElements(totalProducts)
                .totalPages(totalPages)
                .previousOffset(previousOffset)
                .nextOffset(nextOffset)
                .hasPreviousPage(currentPage > 0)
                .hasNextPage(currentPage < totalPages - 1)
                .build();
    }

    private AllProductResDto.PaginationMetadata buildPaginationMetadataFromRepo(Page<ProductDbEntity> productPage) {
        int pageSize = productPage.getSize();
        int currentPage = productPage.getNumber(); // 0-based

        Integer previousOffset = currentPage > 0 ? (currentPage - 1) * pageSize : null;
        Integer nextOffset = productPage.hasNext() ? (currentPage + 1) * pageSize : null;

        return AllProductResDto.PaginationMetadata.builder()
                .currentPage(currentPage + 1)
                .pageSize(pageSize)
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .previousOffset(previousOffset)
                .nextOffset(nextOffset)
                .hasPreviousPage(productPage.hasPrevious())
                .hasNextPage(productPage.hasNext())
                .build();
    }

    private void validateRequestToken(String token) {
        validationUtils.accessTokenValidation(token);
    }

    private void validateBruteForceProtection(String uuid) {
        validationUtils.bruteForceProtection(AppConfig.GET_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private int getValidLimit(Integer limit) {
        // Cap the limit at a maximum of 20
        return limit == null ? 10 : Math.max(1, Math.min(limit, 20));
    }

    private Page<ProductDbEntity> findByOrganisationIdAndIsActive(UUID organisationId, Pageable pageable) {
        return productsRepo.findByOrganisationIdAndIsActive(organisationId, true, pageable)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.PRODUCT_NOT_FOUND_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }
}
