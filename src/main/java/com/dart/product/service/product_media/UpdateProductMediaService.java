package com.dart.product.service.product_media;

import com.dart.product.di.ServiceLocator;
import com.dart.product.entity.product_media_model.*;
import com.dart.product.utilities.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UpdateProductMediaService {

    private final ServiceLocator serviceLocator;

    public UpdateProductMediaService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ResponseEntity<ProductMediaResModel> updateProductMedia(MultipartFile file, String token, Integer id) throws IOException {
        String jwtToken = serviceLocator.getJwtService().extractTokenFromHeader(token);
        String roles = serviceLocator.getJwtService().extractRole(jwtToken);
        String plainUUID = serviceLocator.getJwtService().extractUUID(jwtToken);
        UUID organisationId = serviceLocator.getUtilitiesManager().convertStringToUUID(serviceLocator.getJwtService().extractUUID(jwtToken));

        validateRequestToken(token);
        validateRequestMediaBruceForce(plainUUID);

        //validate the file from inside the login
        MediaUploadResponse mediaFile = serviceLocator.getMediaService().uploadFile(file);

        MediaDbModel isProductMediaExisting = findByIdAndOrganisationIdAndIsActive(id, organisationId);
        isMediaTypeValid(serviceLocator.getUtilitiesManager().getFileExtension(file.getOriginalFilename()), isProductMediaExisting.getMediaType(), file);

        MediaDbModel mediaRecord = serviceLocator.getProductMappers().productMediaBuilder(isProductMediaExisting, mediaFile.getFileName());
        SaveAndUpdateMediaResponse saveResult = serviceLocator.getSaveAndUpdateRecord().saveProductMedia(mediaRecord);
        handleSaveResult(saveResult);

        boolean cacheResult = serviceLocator.getRedisProductCacheRepo().saveUpdateProductMedia(serviceLocator.getProductMappers().toCacheProductMedia(saveResult.getProductMedia()));

        handleCacheResult(cacheResult);
        serviceLocator.getMediaService().deleteImage(isProductMediaExisting.getMediaUrl());
        return buildSuccessResponse(saveResult.getProductMedia());
    }

    private void validateRequestToken(String token) {
        serviceLocator.getValidationUtils().jwtValidateRequest(token);
    }

    private void validateRequestMediaBruceForce(String uuid) {
        serviceLocator.getValidationUtils().bruteForceProtection(AppConfig.FETCH_ALL_PRODUCT_BRUTE_FORCE_PROTECTION + uuid);
    }

    private MediaDbModel findByIdAndOrganisationIdAndIsActive(Integer id, UUID organisationId) {
        return serviceLocator.getProductMediaRepo().findByIdAndOrganisationIdAndIsActive(id, organisationId, true)
                .orElseThrow(() -> new CustomRuntimeException(
                        new ErrorHandler(false, "jjnjn", AppConfig.UPDATE_PRODUCT_ERROR_RESPONSE),
                        HttpStatus.NOT_FOUND
                ));
    }

    private void isMediaTypeValid(String curExt, String oldFileMediaType, MultipartFile file) {
        if (!validateMediaType(curExt, oldFileMediaType, file)) {
            String errorMessage = (oldFileMediaType.equalsIgnoreCase("video"))
                    ? "You cannot replace video media with image media."
                    : "You cannot replace image media with video media.";

            throw new CustomRuntimeException(
                    new ErrorHandler(false, AppConfig.UPDATE_PRODUCT_ERROR_TAG, errorMessage),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void handleCacheResult(boolean cacheResult) {
        if (!cacheResult) {
            // Handle cache save failure
        }
    }

    private boolean validateMediaType(String curExt, String oldFileMediaType, MultipartFile file) {

        if (oldFileMediaType.equalsIgnoreCase("image")) {
            if (file.getSize() > AppConfig.MAX_IMAGE_SIZE) {
                throw new CustomRuntimeException(
                        new ErrorHandler(false, AppConfig.MAX_SIZE_TAG, AppConfig.MAX_SIZE_IMAGE),
                        HttpStatus.BAD_REQUEST
                );
            }
        } else if (oldFileMediaType.equalsIgnoreCase("video")) {
            if (file.getSize() > AppConfig.MAX_VIDEO_SIZE) {
                throw new CustomRuntimeException(
                        new ErrorHandler(false, AppConfig.MAX_SIZE_TAG, AppConfig.MAX_SIZE_VIDEO),
                        HttpStatus.BAD_REQUEST
                );
            }
        }

        if (oldFileMediaType.equalsIgnoreCase("video")) {

            return serviceLocator.getUtilitiesManager().isVideoFile(curExt);
        }
        if (oldFileMediaType.equalsIgnoreCase("image")) {
            return serviceLocator.getUtilitiesManager().isImageFile(curExt);
        }
        return false;
    }

    private ResponseEntity<ProductMediaResModel> buildSuccessResponse(MediaDbModel media) {
        return new ResponseEntity<>(
                new ProductMediaResModel(
                        true,
                        AppConfig.UPDATED_PRODUCT_MEDIA,
                        serviceLocator.getProductMappers().toProductMediaResponse(media)
                ),
                HttpStatus.OK
        );
    }

    private void handleSaveResult(SaveAndUpdateMediaResponse saveResult) {
        if (!saveResult.getStatus()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, saveResult.getError(), saveResult.getError()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
