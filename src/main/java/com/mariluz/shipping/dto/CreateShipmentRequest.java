package com.mariluz.shipping.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateShipmentRequest {

    @NotNull(message = "El ID de venta es obligatorio")
    @Positive(message = "El ID de venta debe ser positivo")
    private Integer saleId;

    // datos de la dirección de envío
    @Valid // propaga validaciones a los campos de ShippingAddressRequest
    @NotNull(message = "Los datos de la dirección de envío son obligatorios")
    private ShippingAddressRequest address;
}
