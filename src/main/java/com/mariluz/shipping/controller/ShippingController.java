package com.mariluz.shipping.controller;

import com.mariluz.shipping.dto.*;
import com.mariluz.shipping.service.ShippingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ShipmentResponse> createShipment(
        @Valid @RequestBody CreateShipmentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            service.createShipment(request)
        );
    }

    // 2. cambiar estado envio (admin)
    @PutMapping("/update-status")
    public ResponseEntity<ShipmentResponse> updateShipmentStatus(
        @Valid @RequestBody UpdateShipmentStatusRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            service.updateShipmentStatus(request)
        );
    }

    // 3. obtener mis envios
    @GetMapping("/my-shipments")
    public ResponseEntity<MyShipmentsResponse> getMyShipments() {
        return ResponseEntity.ok(service.getMyShipments());
    }

    // 4. cancelar pedido (para conectar con sales-service)
    @PutMapping("/cancel/{saleId}")
    public ResponseEntity<?> cancelShipment(
        @Valid @PathVariable Integer saleId
    ) {
        service.cancelShipment(saleId);
        return ResponseEntity.noContent().build();
    }

    // 5. listar envios (admin)
    @GetMapping("/shipments")
    public ResponseEntity<ShipmentsResponse> getShipments() {
        return ResponseEntity.ok(service.getShipments());
    }
}
