package com.mariluz.shipping.dto;

import com.mariluz.shipping.model.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateShipmentStatusRequest {

    @Positive(message = "El id del envío debe ser un número positivo")
    @NotNull(message = "El id del envío es obligatorio")
    private Integer shipmentId;

    @NotNull(message = "El estado es obligatorio")
    private Status status;
}
