package com.mariluz.shipping.service;

import com.mariluz.shipping.dto.*;

public interface ShippingService {
    // 1. crear un envio
    ShipmentResponse createShipment(CreateShipmentRequest request);
    // 2. cambiar estado envio (admin)
    ShipmentResponse updateShipmentStatus(UpdateShipmentStatusRequest request);
    // 3. obtener mis envios
    MyShipmentsResponse getMyShipments();
    // 4. cancelar pedido (para conectar con sales-service)
    void cancelShipment(Integer saleId);
    // 5. listar envios (admin)
    ShipmentsResponse getShipments();
}
