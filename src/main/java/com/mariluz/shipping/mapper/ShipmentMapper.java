/* Mapper para mapear entre entidades y DTOs */

package com.mariluz.shipping.mapper;

import com.mariluz.shipping.dto.CreateShipmentRequest;
import com.mariluz.shipping.dto.MyShipmentsResponse;
import com.mariluz.shipping.dto.ShipmentResponse;
import com.mariluz.shipping.dto.ShippingAddressRequest;
import com.mariluz.shipping.dto.ShippingAddressResponse;
import com.mariluz.shipping.dto.UpdateShipmentStatusRequest;
import com.mariluz.shipping.model.Shipment;
import com.mariluz.shipping.model.ShippingAddress;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {
    // ---------- Request -> Entity ----------
    @Mapping(target = "id", ignore = true) // lo genera la BD
    @Mapping(target = "saleId", source = "request.saleId")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "status", constant = "CREATED") // estado inicial (regla de negocio)
    @Mapping(target = "shippingAddress", source = "request.address") // nombres distintos
    @Mapping(target = "createdAt", ignore = true) // lo crea @CreationTimestamp
    @Mapping(target = "updatedAt", ignore = true) // lo crea @UpdateTimestamp
    Shipment toEntity(CreateShipmentRequest request, String userId);

    ShippingAddress toEmbedded(ShippingAddressRequest request);

    // ---------- Entity -> Response ----------
    @Mapping(target = "address", source = "shippingAddress") // nombres distintos
    ShipmentResponse toResponse(Shipment shipment);

    ShippingAddressResponse toAddressResponse(ShippingAddress address);

    // ---------- Partial Update (solo status) ----------
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    void updateStatus(
        @MappingTarget Shipment shipment,
        UpdateShipmentStatusRequest request
    );

    // ---------- Entity List -> Response Wrapper ----------
    List<ShipmentResponse> toResponseList(List<Shipment> shipments);

    default MyShipmentsResponse toMyShipmentsResponse(List<Shipment> shipments) {
        MyShipmentsResponse response = new MyShipmentsResponse();
        response.setShipments(toResponseList(shipments));
        return response;
    }
}
