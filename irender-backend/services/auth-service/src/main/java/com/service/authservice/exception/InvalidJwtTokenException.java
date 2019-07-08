package com.service.authservice.exception;

public class InvalidJwtTokenException extends RuntimeException {

    private static final long serialVersionUID = -294671188037098603L;

    public static final String MESSAGE = "Jwt Invalid!";

    public InvalidJwtTokenException(String msg) {
        super(msg);
    }

}
