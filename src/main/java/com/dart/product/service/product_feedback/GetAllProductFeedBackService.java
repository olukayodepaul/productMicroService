package com.dart.product.service.product_feedback;


import com.dart.product.dto_model.product_feedback.FetchAllProductFeedBackModel;
import com.dart.product.dto_model.product_feedback.ProductFeedBackAllResDTO;
import com.dart.product.entity.product_feedback_entity.ProductFeedBackCacheEntity;
import com.dart.product.entity.product_feedback_entity.ProductFeedBackDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductFeedBackRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
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
public class GetAllProductFeedBackService {

    @Value("${pagination.maxOffset}")
    private int maxOffset;

    private final ProductFeedBackRepo productFeedBackRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    public GetAllProductFeedBackService(
            ProductFeedBackRepo productFeedBackRepo,
            FilterService jwtService,
            UtilitiesManager utilitiesManager,
            ProductMappers productMappers,
            RedisProductCacheRepo redisProductCacheRepo,
            ValidationUtils validationUtils
    ) {
        this.productFeedBackRepo = productFeedBackRepo;
        this.jwtService = jwtService;
        this.utilitiesManager = utilitiesManager;
        this.productMappers = productMappers;
        this.redisProductCacheRepo = redisProductCacheRepo;
        this.validationUtils = validationUtils;
    }

    public ResponseEntity<ProductFeedBackAllResDTO> getAllProductFeedBack(String authToken, int offset, int limit) {

        validateRequestToken(authToken);

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));

        validateBruteForceProtection(userId.toString());
        validationUserRole(roles);

        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        limit = getValidLimit(limit);
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.ASC, "id"));

        FetchAllProductFeedBackModel getAllCacheRecord = redisProductCacheRepo.findAllProductFeedBack(organisationId.toString());

        List<ProductFeedBackDbEntity> itemFilter;
        ProductFeedBackAllResDTO.PaginationMetadata pagination;

        if(getAllCacheRecord.getStatus()) {

            int totalProducts = getAllCacheRecord.getProductFeedBack().size();
            int start = Math.min(offset, totalProducts);
            int end = Math.min(start + limit, totalProducts);

            if (start >= totalProducts) {
                itemFilter = List.of();
            } else {
                List<ProductFeedBackCacheEntity> paginatedProductsComment = getAllCacheRecord.getProductFeedBack().subList(start, end);
                itemFilter = productMappers.mapToAllProductFeedBack(paginatedProductsComment, pageable).getContent();
            }

            pagination = buildPaginationMetadataFromCache(totalProducts, limit, offset);

        }else{
            Page<ProductFeedBackDbEntity> getPersistedRecord = findByOrganisationId( organisationId, pageable);
            itemFilter = getPersistedRecord.getContent();
            pagination = buildPaginationMetadataFromRepo(getPersistedRecord);
        }

        List<ProductFeedBackAllResDTO.ProductFeedBack> productList = itemFilter.stream()
                .map(productMappers::mapToAllProductFeedBack)
                .collect(Collectors.toList());

        ProductFeedBackAllResDTO response = ProductFeedBackAllResDTO.builder()
                .status(true)
                .message(AppConfig.GET_PRODUCT_RESPONSE)
                .product_feedback(productList)
                .pagination(pagination)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ProductFeedBackAllResDTO.PaginationMetadata buildPaginationMetadataFromCache(int totalProducts, int limit, int offset) {
        int totalPages = (int) Math.ceil((double) totalProducts / limit); // Total pages calculation
        int currentPage = offset / limit; // 0-based current page

        Integer previousOffset = currentPage > 0 ? (currentPage - 1) * limit : null;
        Integer nextOffset = currentPage < totalPages - 1 ? (currentPage + 1) * limit : null;

        return ProductFeedBackAllResDTO.PaginationMetadata.builder()
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

    private ProductFeedBackAllResDTO.PaginationMetadata buildPaginationMetadataFromRepo(Page<ProductFeedBackDbEntity> productPage) {
        int pageSize = productPage.getSize();
        int currentPage = productPage.getNumber(); // 0-based

        Integer previousOffset = currentPage > 0 ? (currentPage - 1) * pageSize : null;
        Integer nextOffset = productPage.hasNext() ? (currentPage + 1) * pageSize : null;

        return ProductFeedBackAllResDTO.PaginationMetadata.builder()
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
        validationUtils.jwtValidateRequest(token);
    }

    private void validationUserRole(String role) {
        validationUtils.userRoleValidation(role);
    }

    private void validateBruteForceProtection(String uuid) {
        validationUtils.bruteForceProtection(AppConfig.GET_ALL_PRODUCT_FEEDBACK_BRUTE_FORCE_PROTECTION + uuid);
    }

    private int getValidLimit(Integer limit) {
        return limit == null ? 10 : Math.max(1, Math.min(limit, maxOffset));
    }

    private Page<ProductFeedBackDbEntity> findByOrganisationId(UUID organisationId, Pageable pageable) {
        return productFeedBackRepo.findByOrganisationId(organisationId, pageable)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.INVALID_RESOURCES_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}
