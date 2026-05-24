package com.mariluz.shipping.exceptions;

public class CouldNotCancelShipmentException extends RuntimeException {

    public CouldNotCancelShipmentException(String message) {
        super(message);
    }
}
