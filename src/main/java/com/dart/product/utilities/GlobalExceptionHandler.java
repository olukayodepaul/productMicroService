package com.dart.product.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle CustomRuntimeException
    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleCustomRuntimeException(CustomRuntimeException ex) {
        // Constructing the ErrorResponse from the exception
        ErrorResponse response = new ErrorResponse(
                ex.getResponseHandler().isStatus(), // Success status from ErrorHandler
                ex.getResponseHandler().getError(), // Error message from ErrorHandler
                ex.getMessage() // Detailed message from the exception
        );

        return new ResponseEntity<>(response, ex.getStatus());
    }

    // Handle general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse response = new ErrorResponse(
                false, // Status
                "INTERNAL_SERVER_ERROR", // Error tag for internal errors
                "An unexpected error occurred." // General error message
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
