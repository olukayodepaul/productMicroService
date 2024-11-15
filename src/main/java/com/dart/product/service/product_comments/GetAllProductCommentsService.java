package com.dart.product.service.product_comments;

import com.dart.product.di.ServicesDi;
import com.dart.product.dto_model.product_comments_model.*;
import com.dart.product.entity.product_comment_entity.ProductCommentCacheEntity;
import com.dart.product.entity.product_comment_entity.ProductCommentDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductCommentRepo;
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
public class GetAllProductCommentsService {

    @Value("${pagination.maxOffset}")
    private int maxOffset;

    private final ProductCommentRepo productCommentRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    private static final Logger logger = LoggerFactory.getLogger(GetAllProductCommentsService.class);

    public GetAllProductCommentsService(ProductCommentRepo productCommentRepo, ServicesDi servicesDi) {
        this.productCommentRepo = productCommentRepo;
        this.jwtService = servicesDi.jwtService();
        this.utilitiesManager = servicesDi.utilitiesManager();
        this.productMappers = servicesDi.productMappers();
        this.redisProductCacheRepo = servicesDi.redisProductCacheRepo();
        this.validationUtils = servicesDi.validationUtils();
    }

    public ResponseEntity<AllProductCommentAllResDTO> getAllProductComment(String authToken, Integer productId, int offset, int limit) {

        validateRequestToken(authToken);
        validProductId(productId);

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validationUserRole(roles);
        validateBruteForceProtection(userId.toString());

        limit = getValidLimit(limit);
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.ASC, "id"));
        FetchAllProductCommentModel getAllCacheRecord = redisProductCacheRepo.findAllProductComment(organisationId.toString(), productId);

        List<ProductCommentDbEntity> itemFilter;
        AllProductCommentAllResDTO.PaginationMetadata pagination;


        if(getAllCacheRecord.getStatus()){

            int totalProducts = getAllCacheRecord.getProductComment().size();
            int start = Math.min(offset, totalProducts);
            int end = Math.min(start + limit, totalProducts);

            if (start >= totalProducts) {
                itemFilter = List.of();
            } else {

                List<ProductCommentCacheEntity> paginatedProductsComment = getAllCacheRecord.getProductComment().subList(start, end);
                itemFilter = productMappers.allProductComment(paginatedProductsComment, pageable).getContent();
            }

            pagination = buildPaginationMetadataFromCache(totalProducts, limit, offset);

        }else{
            Page<ProductCommentDbEntity> getPersistedRecord = findByIdAndOrganisationIdAndIsActive(productId, organisationId, pageable);
            itemFilter = getPersistedRecord.getContent();
            pagination = buildPaginationMetadataFromRepo(getPersistedRecord);
        }


        List<AllProductCommentAllResDTO.ProductComment> productList = itemFilter.stream()
                .map(productMappers::mapToAllProductComment)
                .collect(Collectors.toList());

        AllProductCommentAllResDTO response = AllProductCommentAllResDTO.builder()
                .status(true)
                .message(AppConfig.GET_PRODUCT_RESPONSE)
                .product_comments(productList)
                .pagination(pagination)
                .build();


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private AllProductCommentAllResDTO.PaginationMetadata buildPaginationMetadataFromCache(int totalProducts, int limit, int offset) {
        int totalPages = (int) Math.ceil((double) totalProducts / limit); // Total pages calculation
        int currentPage = offset / limit; // 0-based current page

        Integer previousOffset = currentPage > 0 ? (currentPage - 1) * limit : null;
        Integer nextOffset = currentPage < totalPages - 1 ? (currentPage + 1) * limit : null;

        return AllProductCommentAllResDTO.PaginationMetadata.builder()
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

    private AllProductCommentAllResDTO.PaginationMetadata buildPaginationMetadataFromRepo(Page<ProductCommentDbEntity> productPage) {
        int pageSize = productPage.getSize();
        int currentPage = productPage.getNumber(); // 0-based

        Integer previousOffset = currentPage > 0 ? (currentPage - 1) * pageSize : null;
        Integer nextOffset = productPage.hasNext() ? (currentPage + 1) * pageSize : null;

        return AllProductCommentAllResDTO.PaginationMetadata.builder()
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

    private void validProductId(Integer productId) {
        validationUtils.validProductId(productId);
    }

    private void validateRequestToken(String token) {
        validationUtils.jwtValidateRequest(token);
    }

    private void validationUserRole(String role) {
        validationUtils.userRoleValidation(role);
    }

    private void validateBruteForceProtection(String userId) {
        validationUtils.bruteForceProtection(AppConfig.GET_ALL_PRODUCT_COMMENT_BRUTE_FORCE_PROTECTION + userId);
    }

    private int getValidLimit(Integer limit) {
        return limit == null ? 10 : Math.max(1, Math.min(limit, maxOffset));
    }

    private Page<ProductCommentDbEntity> findByIdAndOrganisationIdAndIsActive(Integer productId, UUID organisationId, Pageable pageable) {
        return productCommentRepo.findByProductIdAndOrganisationIdAndIsActive( productId, organisationId, true, pageable)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.INVALID_RESOURCES_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

}

