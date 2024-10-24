package com.dart.product.utilities;

import com.dart.product.entity.product_model.ProductReqModel;
import com.dart.product.rate_limit.BruteForceRateLimitService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class ValidationUtils {

    private final EmailValidator emailValidator;
    private final BCryptPasswordEncoder encoder;
    private final BruteForceRateLimitService rateLimitService;

    public ValidationUtils(
            EmailValidator emailValidator,
            BruteForceRateLimitService rateLimitService
    ) {
        this.emailValidator = emailValidator;
        this.encoder = new BCryptPasswordEncoder(12);
        this.rateLimitService = rateLimitService;
    }

    public void jwtValidateRequest(String token) {
        validateField(token, "Access Token");
    }

    public void mediaTypeValidation(String mediaType) {
        validateField(mediaType, "Media Type");
    }

    public void productIdValidation(Integer productId) {
        validateField(productId, "Product Id");
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

    public void userRoleValidateRequest(String role) {
        validateField(role, "Role");
    }

    public void productValidateRequest(ProductReqModel request, int target) {
        validateField(request.getName(), "Product Name");
        validateField(request.getDescription(), "Description");
        validateField(request.getPrice(), "Price");
        validateField(request.getDiscount(), "Discount");
        validateField(request.getCategory_id(), "Product Category");
    }

    //u can add more access role for users
    public void roleValidation(String role) {
        if(!role.equalsIgnoreCase("sys_admin")){
            throw new CustomRuntimeException(new ErrorHandler(false, "Administrative role"," Administrative protected role"), HttpStatus.BAD_REQUEST);
        }
    }

    public static void validateField(Object field, String fieldName) {
        if (field == null) {
            throw new CustomRuntimeException(new ErrorHandler(false, "validation error", fieldName + " cannot be null"), HttpStatus.BAD_REQUEST);
        }
    }

    public void bruteForceProtection(String uuid) {
        if (rateLimitService.isRateLimited(uuid)) {
            throw new CustomRuntimeException(
                    new ErrorHandler(false, "Rate limit exceeded", "You have exceeded the maximum number of requests per minute."),
                    HttpStatus.TOO_MANY_REQUESTS
            );
        }
    }


}