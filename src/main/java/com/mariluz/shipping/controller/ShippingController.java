package com.mariluz.shipping.controller;

import com.mariluz.shipping.dto.*;
import com.mariluz.shipping.service.ShippingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipping")
@RequiredArgsConstructor
@Validated
public class ShippingController {

    private final ShippingService service;

    // 1. crear un envio
    @PostMapping("/create")
    public ShipmentResponse createShipment(
        @Valid @RequestBody CreateShipmentRequest request
    ) {
        return service.createShipment(request);
    }

    // 2. cambiar estado envio (admin)
    @PutMapping("/update-status")
    public ShipmentResponse updateShipmentStatus(
        @Valid @RequestBody UpdateShipmentStatusRequest request
    ) {
        return service.updateShipmentStatus(request);
    }

    // 3. obtener mis envios
    @GetMapping("/my-shipments")
    public MyShipmentsResponse getMyShipments() {
        return service.getMyShipments();
    }

    // 4. cancelar pedido (para conectar con sales-service)
    @PostMapping("/cancel/{shipmentId}")
    public void cancelShipment(@Valid @PathVariable Integer shipmentId) {
        service.cancelShipment(shipmentId);
    }
}
