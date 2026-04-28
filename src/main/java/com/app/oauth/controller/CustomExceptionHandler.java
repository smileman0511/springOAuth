package com.app.oauth.controller;

import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.exception.JwtTokenException;
import com.app.oauth.exception.MemberException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ApiResponseDTO> handleMemberException(MemberException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(ApiResponseDTO.of(e.getMessage()));
    }

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<ApiResponseDTO> handleJwtTokenException(JwtTokenException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(ApiResponseDTO.of(e.getMessage()));
    }

}
