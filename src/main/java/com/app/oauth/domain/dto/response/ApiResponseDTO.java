package com.app.oauth.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponseDTO(String message) {
        this.message = message;
    }

    public ApiResponseDTO(String message, T data) {
        this.message = message;
        this.data = data;
    }
    public ApiResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static <T> ApiResponseDTO<T> of(String message) {
        return new ApiResponseDTO<>(message);
    }

    public static <T> ApiResponseDTO<T> of(String message, T data) {
        return new ApiResponseDTO<>(message, data);
    }

    public static <T> ApiResponseDTO<T> of(boolean success ,String message) {
        return new ApiResponseDTO<>(success, message);
    }

}
