package com.dart.product.service.product_media;


import com.dart.product.dto_model.product_media_model.FetchAllProductMediaModel;
import com.dart.product.dto_model.product_media_model.GetProductMediaByOrganisationResDTO;
import com.dart.product.entity.prodct_media.MediaDbEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductMediaRepo;
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


@Service
public class GetAllProductMediaByOrganisation {

    @Value("${pagination.maxOffset}")
    private int maxOffset;

    private final ProductMappers productMappers;
    private final UtilitiesManager utilitiesManager;
    private final ValidationUtils validationUtils;
    private final FilterService jwtService;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ProductMediaRepo productMediaRepo;

    public GetAllProductMediaByOrganisation(
            UtilitiesManager utilitiesManager,
            ValidationUtils validationUtils,
            FilterService jwtService,
            ProductMappers productMappers,
            RedisProductCacheRepo redisProductCacheRepo,
            ProductMediaRepo productMediaRepo
    ) {
        this.utilitiesManager = utilitiesManager;
        this.validationUtils = validationUtils;
        this.jwtService = jwtService;
        this.productMappers = productMappers;
        this.redisProductCacheRepo = redisProductCacheRepo;
        this.productMediaRepo = productMediaRepo;
    }

    public ResponseEntity<GetProductMediaByOrganisationResDTO> getProductMediaByOrganisation(String authToken, int offset, int limit) {

        String jwtToken = jwtService.extractTokenFromHeader(authToken);
        String roles = jwtService.extractRole(jwtToken);
        UUID userId = utilitiesManager.convertStringToUUID(jwtService.extractUserId(jwtToken));
        validateBruteForceProtection(userId.toString());
        UUID organisationId = utilitiesManager.convertStringToUUID(jwtService.extractOrganisationId(jwtToken));

        validateRequestToken(authToken);
        validateUserRole(roles);

        limit = getValidLimit(limit);
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.ASC, "id"));

        FetchAllProductMediaModel cachedProductMedia = redisProductCacheRepo.findAllProductMediaByOrganisationId(organisationId.toString());


        List<MediaDbEntity> itemFilter;
        GetProductMediaByOrganisationResDTO.PaginationMetadata pagination;


        if (cachedProductMedia.getStatus()) {

            List<MediaDbEntity> allProducts = productMappers.mapProductMediaCacheToProductDTO(cachedProductMedia.getProductMedia());
            int totalProducts = allProducts.size();
            int start = Math.min(offset, totalProducts);
            int end = Math.min(start + limit, totalProducts);

            itemFilter = (start >= totalProducts) ?
                    List.of() :
                    allProducts.subList(start, end);

            pagination = buildPaginationMetadataFromCache(totalProducts, limit, offset);

        } else {
            Page<MediaDbEntity> persistedProducts = getPersistedProductMedia(organisationId, pageable);

            System.out.println("Page Content: " + persistedProducts.getContent());
            System.out.println("Total Elements: " + persistedProducts.getTotalElements());
            System.out.println("Total Pages: " + persistedProducts.getTotalPages());
            System.out.println("Current Page: " + persistedProducts.getNumber());
            System.out.println("Page Size: " + persistedProducts.getSize());

            itemFilter = persistedProducts.getContent();

            pagination = buildPaginationMetadataFromRepo(persistedProducts);
        }

        return new ResponseEntity<>(productMappers.mapProductMediaByOrganisation(itemFilter, pagination, AppConfig.PRODUCT_MEDIA_FETCH_RESPONSE), HttpStatus.OK);
    }

    private void validateRequestToken(String token) {
        validationUtils.accessTokenValidation(token);
    }

    private void validateUserRole(String role) {
        validationUtils.adminRoleValidation(role);
    }

    private void validateBruteForceProtection(String userId) {
        validationUtils.bruteForceProtection(AppConfig.FETCH_PRIMARY_PRODUCT_MEDIA_BY_ORGANISATION_ID_BRUTE_FORCE_PROTECTION + userId);
    }

    private int getValidLimit(Integer limit) {
        return limit == null ? 10 : Math.max(1, Math.min(limit, maxOffset));
    }

    private Page<MediaDbEntity> getPersistedProductMedia(UUID organisationId, Pageable pageable) {
        return productMediaRepo.findByOrganisationIdAndIsActive(organisationId, true, pageable)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.INVALID_RESOURCES_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    private GetProductMediaByOrganisationResDTO.PaginationMetadata buildPaginationMetadataFromCache(int totalProducts, int limit, int offset) {
        int totalPages = (int) Math.ceil((double) totalProducts / limit);
        int currentPage = offset / limit;

        Integer previousOffset = currentPage > 0 ? (currentPage - 1) * limit : null;
        Integer nextOffset = currentPage < totalPages - 1 ? (currentPage + 1) * limit : null;

        return GetProductMediaByOrganisationResDTO.PaginationMetadata.builder()
                .currentPage(currentPage + 1)
                .pageSize(limit)
                .totalElements(totalProducts)
                .totalPages(totalPages)
                .previousOffset(previousOffset)
                .nextOffset(nextOffset)
                .hasPreviousPage(currentPage > 0)
                .hasNextPage(currentPage < totalPages - 1)
                .build();
    }

    private GetProductMediaByOrganisationResDTO.PaginationMetadata buildPaginationMetadataFromRepo(Page<MediaDbEntity> productPage) {
        int pageSize = productPage.getSize();
        int currentPage = productPage.getNumber(); // 0-based

        Integer previousOffset = currentPage > 0 ? (currentPage - 1) * pageSize : null;
        Integer nextOffset = productPage.hasNext() ? (currentPage + 1) * pageSize : null;

        return GetProductMediaByOrganisationResDTO.PaginationMetadata.builder()
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
}
