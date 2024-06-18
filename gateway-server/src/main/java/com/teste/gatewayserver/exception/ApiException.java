package com.teste.gatewayserver.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }
}
