package com.app.oauth.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class JwtTokenException extends RuntimeException{

    private HttpStatus httpStatus;

    public JwtTokenException(String message) {
        super(message);
    }
    public JwtTokenException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
