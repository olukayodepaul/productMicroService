package com.dart.product.service.product_media;


import com.dart.product.entity.product_media_model.*;
import com.dart.product.mapper.ProductMappers;
import com.dart.product.repository.ProductMediaRepo;
import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.security.FilterService;
import com.dart.product.utilities.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;


@Service
public class AddProductMediaService {

    private final MediaService mediaService;
    private final ProductMappers productMappers;
    private final UtilitiesManager utilitiesManager;
    private final ValidationUtils validationUtils;
    private final FilterService jwtService;
    private final SaveAndUpdateRecord saveAndUpdateRecord;
    private final RedisProductCacheRepo redisProductCacheRepo;
    private final ProductMediaRepo productMediaRepo;

    public AddProductMediaService(
            UtilitiesManager utilitiesManager,
            ValidationUtils validationUtils,
            FilterService jwtService,
            MediaService mediaService,
            ProductMappers productMappers,
            SaveAndUpdateRecord saveAndUpdateRecord,
            RedisProductCacheRepo redisProductCacheRepo,
            ProductMediaRepo productMediaRepo
    ) {
        this.utilitiesManager = utilitiesManager;
        this.validationUtils = validationUtils;
        this.jwtService = jwtService;
        this.mediaService = mediaService;
        this.productMappers = productMappers;
        this.saveAndUpdateRecord = saveAndUpdateRecord;
        this.redisProductCacheRepo = redisProductCacheRepo;
        this.productMediaRepo = productMediaRepo;
    }

    public ResponseEntity<ProductMediaResModel> addProductMedia(MultipartFile file, String data, String token) {
        try{

            String jwtToken = jwtService.extractTokenFromHeader(token);
            String roles = jwtService.extractRole(jwtToken);
            String plainUUID = jwtService.extractUUID(jwtToken);
            UUID organisationId =  utilitiesManager.convertStringToUUID(jwtService.extractUUID(jwtToken)); //is a uuid

            validateRequest(token, plainUUID, roles);

            ObjectMapper objectMapper = new ObjectMapper();
            MediaUploadReqModel mediaData = objectMapper.readValue(data, MediaUploadReqModel.class);

            //delete the product if failed
            MediaUploadResponse mediaFile = mediaService.uploadFile(file);

            boolean confirmPrimaryListing = productMediaRepo.findByProductIdAndOrganisationIdAndMediaTypeAndIsPrimary(
                    mediaData.getProductId(),
                    organisationId,
                    mediaFile.getMediaType(),
                    true
            ).isPresent();

            MediaDbModel mediaRecord = productMappers.toProductMedia(mediaData.getProductId(), organisationId, mediaFile.getMediaType(), mediaFile.getFileName(), !confirmPrimaryListing);
            SaveAndUpdateMediaResponse saveResult = saveAndUpdateRecord.saveProductMedia(mediaRecord);

            if(!saveResult.getStatus()) {
                throw new CustomRuntimeException(
                        new ErrorHandler(false, saveResult.getError(), saveResult.getError()),
                        HttpStatus.BAD_REQUEST
                );
            }

            boolean cacheResult = redisProductCacheRepo.saveUpdateProductMedia(productMappers.toCacheProductMedia(saveResult.getProductMedia()));

            if(!cacheResult){
                //save to redis.
            }

            return new ResponseEntity<>(new ProductMediaResModel(
                    true,
                    AppConfig.ADD_PRODUCT_MEDIA,
                    productMappers.toProductMediaResponse(saveResult.getProductMedia())
            ), HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //set initial product as a primary

    private void validateRequest(String token,  String uuid, String role) {
//        validationUtils.userRoleValidateRequest(role);
        validationUtils.jwtValidateRequest(token);
//        validationUtils.roleValidation(role);
        validationUtils.bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

}
