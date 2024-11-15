package com.dart.product.service.product_media;


import com.dart.product.dependency.di.ServicesDi;
import com.dart.product.dto_model.product_media_model.FetchAllProductMediaModel;
import com.dart.product.dto_model.product_media_model.GetProductMediaByOrganisationResDTO;
import com.dart.product.entity.prodct_media.MediaContentDbEntity;
import com.dart.product.entity.prodct_media.MediaDbEntity;
import com.dart.product.entity.prodct_media.ProductContentMediaCacheEntity;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductMediaContentRepo;
import com.dart.product.repository.ProductMediaRepo;
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
public class GetAllProductMediaByOrganisation {

    @Value("${pagination.maxOffset}")
    private int maxOffset;

    private static final Logger logger = LoggerFactory.getLogger(GetAllProductMediaByOrganisation.class);
    private final ProductMediaContentRepo productMediaContentRepo;
    private final ProductMediaRepo productMediaRepo;
    private final FilterService jwtService;
    private final UtilitiesManager utilitiesManager;
    private final ProductMappers productMappers;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ValidationUtils validationUtils;

    public GetAllProductMediaByOrganisation(
            ProductMediaContentRepo productMediaContentRepo,
            ProductMediaRepo productMediaRepo,
            ServicesDi di
    )
    {
        this.productMediaContentRepo = productMediaContentRepo;
        this.productMediaRepo = productMediaRepo;
        this.jwtService = di.jwtService();
        this.utilitiesManager = di.utilitiesManager();
        this.productMappers = di.productMappers();
        this.redisProductCacheRepo = di.redisProductCacheRepo();
        this.validationUtils = di.validationUtils();
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

        List<MediaContentDbEntity> itemFilter;
        GetProductMediaByOrganisationResDTO.PaginationMetadata pagination;

        if (cachedProductMedia.getStatus()) {

            List<MediaDbEntity> allProducts = productMappers.mapProductMediaCachePage(redisProductCacheRepo.findPagingProductMediaByOrganisationId(organisationId.toString()).getProductMedia());

            int totalProducts = allProducts.size();
            int start = Math.min(offset, totalProducts);
            int end = Math.min(start + limit, totalProducts);

            List<MediaDbEntity>  itemFilters = (start >= totalProducts) ?
                    List.of() :
                    allProducts.subList(start, end);

            List<Integer> productIds = itemFilters.stream()
                    .map(MediaDbEntity::getProductId)
                    .distinct()
                    .collect(Collectors.toList());

            List<ProductContentMediaCacheEntity> finds = redisProductCacheRepo.findOnlyFilteredProductMediaByOrganisationId(organisationId.toString(), productIds).getProductMedia();
            itemFilter = productMappers.mapProductMediaCacheToProductDTO(finds);

            pagination = buildPaginationMetadataFromCache(totalProducts, limit, offset);

        } else {

            Page<MediaDbEntity> persistedProducts = getPersistedProductMedia(organisationId, pageable);
            List<MediaDbEntity> itemFilters = persistedProducts.getContent();

            List<Integer> productIds = itemFilters.stream()
                    .map(MediaDbEntity::getProductId)
                    .distinct()
                    .collect(Collectors.toList());

            pagination = buildPaginationMetadataFromRepo(persistedProducts);
            itemFilter  = getPersistedProductMediaContent(organisationId, productIds);

        }

        GetProductMediaByOrganisationResDTO result  = productMappers.mapProductMediaByOrganisation(itemFilter, pagination, AppConfig.PRODUCT_MEDIA_FETCH_RESPONSE);
        return new ResponseEntity<>(result, HttpStatus.OK);

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

    private List<MediaContentDbEntity> getPersistedProductMediaContent(UUID organisationId,  List<Integer> productIdList) {
        return productMediaContentRepo.findByOrganisationIdAndIsActiveAndProductIdIn(organisationId, true, productIdList)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, String.valueOf(HttpStatus.NOT_FOUND), AppConfig.INVALID_RESOURCES_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    private Page<MediaDbEntity> getPersistedProductMedia(UUID organisationId, Pageable pageable) {
        return productMediaRepo.findByOrganisationId(organisationId,  pageable)
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
        int currentPage = productPage.getNumber();

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