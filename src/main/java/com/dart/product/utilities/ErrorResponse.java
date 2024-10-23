package com.dart.product.utilities;


import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ErrorResponse {
    private boolean status; // Corresponds to success in ErrorHandler
    private String error;   // Corresponds to errorTag in ErrorHandler
    private String message; // Corresponds to message in ErrorHandler

}
