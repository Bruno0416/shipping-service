package com.mariluz.shipping.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShippingAddressRequest {

    @NotBlank(message = "La región es obligatoria")
    @Size(max = 80, message = "La región no puede superar los 80 caracteres")
    private String region;

    @NotBlank(message = "La comuna es obligatoria")
    @Size(max = 60, message = "La comuna no puede superar los 60 caracteres")
    private String commune;

    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 100, message = "La calle no puede superar los 100 caracteres")
    private String street;

    @NotBlank(message = "El número es obligatorio")
    @Size(max = 10, message = "El número no puede superar los 10 caracteres")
    private String number;

    @Size(max = 100, message = "La referencia no puede superar los 100 caracteres")
    private String reference;
}
