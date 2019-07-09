package com.service.authservice.exception;

import com.common.irendersecurity.exception.InvalidJwtTokenException;
import com.common.irendersecurity.exception.JwtExpiredTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleInvalidUserPassException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity handleInvalidJwtTokenException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(InvalidJwtTokenException.MESSAGE);
    }

    @ExceptionHandler(JwtExpiredTokenException.class)
    public ResponseEntity handleJwtExpiredTokenException(JwtExpiredTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
