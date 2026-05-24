package com.mariluz.shipping.exceptions;

public class ShipmentNotFoundException extends RuntimeException {

    public ShipmentNotFoundException() {
        super("Envío no encontrado");
    }

    public ShipmentNotFoundException(String message) {
        super(message);
    }
}
