package com.dart.product.utilities;

import com.dart.product.dto_model.product_comments_model.AddProductCommentReqlDTO;
import com.dart.product.dto_model.product_dto_model.ProductReqDTO;
import com.dart.product.dto_model.product_feedback.AddProductFeedBackReqDTO;
import com.dart.product.dto_model.product_specification_model.AddProductSpecReqModel;
import com.dart.product.dto_model.shipping_details_model.AddShippingDetailsReqModel;
import com.dart.product.rate_limit.BruteForceRateLimitService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
public class ValidationUtils {

    private final EmailValidator emailValidator;
    private final BruteForceRateLimitService rateLimitService;
    private final UtilitiesManager utilitiesManager;

    public ValidationUtils(
            EmailValidator emailValidator,
            BruteForceRateLimitService rateLimitService,
            UtilitiesManager utilitiesManager
    ) {
        this.emailValidator = emailValidator;
        this.rateLimitService = rateLimitService;
        this.utilitiesManager = utilitiesManager;
    }

    public void accessTokenValidation(String token) {
        validateField(token, AppConfig.ACCESS_TOKEN_VALIDATION);
    }

    public void productValidateRequest(ProductReqDTO request) {
        validateField(request.getName(), AppConfig.PRODUCT_NAME_VALIDATION);
        validateField(request.getDescription(), AppConfig.PRODUCT_DESCRIPTION_VALIDATION );
        validateField(request.getPrice(), AppConfig.PRODUCT_PRICE_VALIDATION);
        validateField(request.getDiscount(), AppConfig.PRODUCT_DISCOUNT_VALIDATION);
        validateField(request.getCategory_id(), AppConfig.PRODUCT_CATEGORY_VALIDATION);
    }

    public void validProductId(Integer productId) {
        validateField(productId, AppConfig.PRODUCT_ID_VALIDATION);
        if(!utilitiesManager.isWholeNumberGreaterThanZero(productId.toString()) ){
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST),AppConfig.INVALID_PRODUCT_ID),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void validCommentId(Integer id) {
        validateField(id, AppConfig.PRODUCT_COMMENT_ID_VALIDATION);
        if(!utilitiesManager.isWholeNumberGreaterThanZero(id.toString()) ){
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST),AppConfig.INVALID_COMMENT_ID_VALIDATION),
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    public void productCommentValidateRequest(AddProductCommentReqlDTO request) {
        validateField(request.getComment_text(), AppConfig.PRODUCT_COMMENT_VALIDATION );
        if(request.getComment_text().isEmpty()){
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST),AppConfig.EMPTY_PRODUCT_COMMENT_VALIDATION),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void productFeedBackValidateRequest(AddProductFeedBackReqDTO request) {
        validateField(request.getFeedback_type(), AppConfig.PRODUCT_FEEDBACK_TYPE_VALIDATION );
        if(request.getFeedback_type().isEmpty() ||
                (!request.getFeedback_type().equalsIgnoreCase("like")
                && !request.getFeedback_type().equalsIgnoreCase("dislike")
                && !request.getFeedback_type().equalsIgnoreCase("neutral")))
        {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST),AppConfig.PRODUCT_COMMENT_LIKE_DISLIKE_VALIDATION),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void validateProductRecord(ProductReqDTO request) {

        if(request.getName().isEmpty()){
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST),AppConfig.EMPTY_PRODUCT_NAME_VALIDATION),
                    HttpStatus.BAD_REQUEST
            );
        }

        if(request.getDescription().isEmpty()) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST),AppConfig.EMPTY_PRODUCT_DESCRIPTION_VALIDATION),
                    HttpStatus.BAD_REQUEST
            );
        }


        if(!utilitiesManager.isNumber(request.getPrice().toString())){
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST),AppConfig.INVALID_PRODUCT_PRICE),
                    HttpStatus.BAD_REQUEST
            );
        }

        if(!utilitiesManager.isWholeNumberGreaterThanZero(request.getCategory_id().toString())){
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST),AppConfig.INVALID_PRODUCT_CATEGORY),
                    HttpStatus.BAD_REQUEST
            );
        }

        if(!utilitiesManager.isWholeNumberGreaterThanZero(request.getBrand_id().toString()) ){
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST),AppConfig.INVALID_PRODUCT_BRAND_ID),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void adminRoleValidation(String role) {
        if(!role.equalsIgnoreCase("ADMIN")){
            throw new CustomRuntimeException(new ErrorHandler(false, String.valueOf(HttpStatus.FORBIDDEN), AppConfig.PERMISSION_VALIDATION), HttpStatus.FORBIDDEN);
        }
    }

    public void validatePrimaryMedia(String primaryMedia) {
        if(!primaryMedia.equalsIgnoreCase("true") && !primaryMedia.equalsIgnoreCase("false")) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST),AppConfig.VALIDATE_PRIMARY_MEDIA),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void userRoleValidation(String role) {
        if(!role.equalsIgnoreCase("USER")){
            throw new CustomRuntimeException(new ErrorHandler(false, String.valueOf(HttpStatus.FORBIDDEN), AppConfig.PERMISSION_VALIDATION), HttpStatus.FORBIDDEN);
        }
    }

    public void bruteForceProtection(String uuid) {
        if (rateLimitService.isRateLimited(uuid)) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.TOO_MANY_REQUESTS),AppConfig.BRUTE_FORCE_PROTECTION_RESPONSE),
                    HttpStatus.TOO_MANY_REQUESTS
            );
        }
    }

    public void validateProductId(Integer id) {
        validateField(id, AppConfig.PRODUCT_ID_VALIDATION);
    }

    public void mediaTypeValidation(String mediaType) {
        validateField(mediaType, "Media Type");
        if(!mediaType.equalsIgnoreCase("video") && !mediaType.equalsIgnoreCase("image")){
            throw new CustomRuntimeException(
                    new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST),AppConfig.PRODUCT_MEDIA_FETCH),
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    //end here


































    public void jwtValidateRequest(String token) {
        validateField(token, "Access Token");
    }


    public void mediaIdValidation(Integer mediaId) {
        validateField(mediaId, "Media Id");
    }

    public void productIdValidation(Integer productId) {
        validateField(productId, "Product Id");
    }

    public void productSpecValidation(Integer id) {
        validateField(id, "Product specification id");
    }

    public void IdValidation(String token) {
        validateField(token, "Product ID");
    }

    public void sanitizeEmail(String email) {
        if (!emailValidator.isValid(email)) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, "error", "Invalid email format"),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void validatePasswordStrength(String password) {

        if (password.length() < 8) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, "error", "Password must be at least 8 characters long."),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, "error", "Password must contain at least one uppercase letter."),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (!password.matches(".*[a-z].*")) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, "error", "Password must contain at least one lowercase letter."),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (!password.matches(".*\\d.*")) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, "error", "Password must contain at least one digit."),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (!password.matches(".*[@#$%^&+=!].*")) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, "error", "Password must contain at least one special character (@#$%^&+=!)."),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void productSpecValidate(AddProductSpecReqModel request) {
        validateField(request.getProduct_id(), "Product Id");
        validateField(request.getDimensions().getLength(), "length");
        validateField(request.getWeight(), "Weight");
        validateField(request.getDimensions().getHeight(), "Height");
        validateField(request.getDimensions().getWidth(), "Width");
        validateField(request.getCertification_description(), "Certification Description");
        validateField(request.getMaterial_description(), "Material Description");
    }

    public void shippingDetailsValidation(AddShippingDetailsReqModel reqBody){

    }


    //u can add more access role for users
    public void roleValidation(String role) {
        if(!role.equalsIgnoreCase("admin")){
            throw new CustomRuntimeException(new ErrorHandler(false, "Administrative role"," Administrative protected role"), HttpStatus.BAD_REQUEST);
        }
    }

    //u can add more access role for users
    public void customerRoleValidation(String role) {
        if(!role.equalsIgnoreCase("customer")){
            throw new CustomRuntimeException(new ErrorHandler(false, "Administrative role"," Administrative protected role"), HttpStatus.BAD_REQUEST);
        }
    }



    //final here
    public static void validateField(Object field, String fieldName) {
        if (field == null) {
            throw new CustomRuntimeException(new ErrorHandler(false, String.valueOf(HttpStatus.BAD_REQUEST), " "+fieldName +" "+ AppConfig.NULL_VALIDATION), HttpStatus.BAD_REQUEST);
        }
    }


}