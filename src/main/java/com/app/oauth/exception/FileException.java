package com.app.oauth.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class FileException extends RuntimeException{

    private HttpStatus httpStatus;

    public FileException(String message) {
        super(message);
    }
    public FileException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
