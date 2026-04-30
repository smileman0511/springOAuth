// ApiResponseDTO.java
package com.app.oauth.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static <T> ApiResponseDTO<T> of(boolean success, String message) {
        return new ApiResponseDTO<>(success, message);
    }

    public static <T> ApiResponseDTO<T> of(boolean success, String message, T data) {
        return new ApiResponseDTO<>(success, message, data);
    }
}