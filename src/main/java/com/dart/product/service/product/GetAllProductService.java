package com.dart.product.service.product;

import com.dart.product.dependency.di.ServicesDi;
import com.dart.product.dto_model.product_dto_model.AllProductResDTO;
import com.dart.product.dto_model.product_dto_model.FetchAllProductsResModel;
import com.dart.product.entity.product_entity.ProductCacheEntity;
import com.dart.product.entity.product_entity.ProductDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductsRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
public class GetAllProductService {

    private static final Logger logger = LoggerFactory.getLogger(GetAllProductService.class);
    private final ProductsRepo productsRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    @Value("${pagination.maxOffset}")
    private int maxOffset;

    public GetAllProductService(ProductsRepo productsRepo, ServicesDi di){
        this.productsRepo = productsRepo;
        this.jwtService = di.jwtService();
        this.utilitiesManager = di.utilitiesManager();
        this.productMappers = di.productMappers();
        this.redisProductCacheRepo = di.redisProductCacheRepo();
        this.validationUtils = di.validationUtils();
    }

    public ResponseEntity<AllProductResDTO> getAllProduct(String authToken, int offset, int limit) {

        validateRequestToken(authToken);
        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        String roles = jwtService.extractRole(jwtToken);
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));
        validationUserRole(roles);
        validateBruteForceProtection(userId.toString());

        limit = getValidLimit(limit);
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.ASC, "id"));
        FetchAllProductsResModel getAllCacheRecord = redisProductCacheRepo.getAllProducts(organisationId.toString());

        List<ProductDbEntity> itemFilter;
        AllProductResDTO.PaginationMetadata pagination;

        if (getAllCacheRecord.getStatus()) {

            int totalProducts = getAllCacheRecord.getProduct().size();
            int start = Math.min(offset, totalProducts);
            int end = Math.min(start + limit, totalProducts);

            if (start >= totalProducts) {
                itemFilter = List.of();
            } else {
                List<ProductCacheEntity> paginatedProducts = getAllCacheRecord.getProduct().subList(start, end);
                itemFilter = productMappers.toCacheFromProduct(paginatedProducts, pageable).getContent();
            }

            pagination = buildPaginationMetadataFromCache(totalProducts, limit, offset);

        } else {

            Page<ProductDbEntity> productPage = findByOrganisationIdAndIsActive(organisationId, pageable);
            itemFilter = productPage.getContent();
            pagination = buildPaginationMetadataFromRepo(productPage);

        }

        List<AllProductResDTO.Product> productList = itemFilter.stream()
                .map(productMappers::toProductDto)
                .collect(Collectors.toList());

        AllProductResDTO response = AllProductResDTO.builder()
                .status(true)
                .message(AppConfig.GET_PRODUCT_RESPONSE)
                .products(productList)
                .pagination(pagination)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private AllProductResDTO.PaginationMetadata buildPaginationMetadataFromCache(int totalProducts, int limit, int offset) {
        int totalPages = (int) Math.ceil((double) totalProducts / limit);
        int currentPage = offset / limit; // 0-based current page

        Integer previousOffset = currentPage > 0 ? (currentPage - 1) * limit : null;
        Integer nextOffset = currentPage < totalPages - 1 ? (currentPage + 1) * limit : null;

        return AllProductResDTO.PaginationMetadata.builder()
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

    private AllProductResDTO.PaginationMetadata buildPaginationMetadataFromRepo(Page<ProductDbEntity> productPage) {
        int pageSize = productPage.getSize();
        int currentPage = productPage.getNumber();

        Integer previousOffset = currentPage > 0 ? (currentPage - 1) * pageSize : null;
        Integer nextOffset = productPage.hasNext() ? (currentPage + 1) * pageSize : null;

        return AllProductResDTO.PaginationMetadata.builder()
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

    private void validationUserRole(String role) {
        validationUtils.adminRoleValidation(role);
    }

    private void validateRequestToken(String token) {
        validationUtils.accessTokenValidation(token);
    }

    private void validateBruteForceProtection(String uuid) {
        validationUtils.bruteForceProtection(AppConfig.GET_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private int getValidLimit(Integer limit) {
        return limit == null ? 10 : Math.max(1, Math.min(limit, maxOffset));
    }

    private Page<ProductDbEntity> findByOrganisationIdAndIsActive(UUID organisationId, Pageable pageable) {
        return productsRepo.findByOrganisationIdAndIsActive(organisationId, true, pageable)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.INVALID_RESOURCES_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }
}
