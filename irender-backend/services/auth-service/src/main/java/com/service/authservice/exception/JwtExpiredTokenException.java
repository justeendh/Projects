package com.service.authservice.exception;

public class JwtExpiredTokenException extends RuntimeException {

    private static final long serialVersionUID = -5959543783324224864L;

    public static final String MESSAGE = "Jwt Token expired";

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }

    public JwtExpiredTokenException(String msg, Throwable t) {
        super(msg, t);
    }
}
