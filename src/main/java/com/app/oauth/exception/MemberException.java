package com.app.oauth.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class MemberException extends RuntimeException{

    private HttpStatus httpStatus;

    public MemberException(String message) {
        super(message);
    }
    public MemberException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
