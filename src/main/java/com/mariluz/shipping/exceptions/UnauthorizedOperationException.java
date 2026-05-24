package com.mariluz.shipping.exceptions;

// excepción para manejo de permisos
public class UnauthorizedOperationException extends RuntimeException {

    public UnauthorizedOperationException(String message) {
        super(message);
    }
}
