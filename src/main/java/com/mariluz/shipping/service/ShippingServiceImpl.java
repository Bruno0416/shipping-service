package com.mariluz.shipping.service;

import com.mariluz.shipping.dto.*;
import com.mariluz.shipping.exceptions.CouldNotCancelShipmentException;
import com.mariluz.shipping.exceptions.CouldNotCreateShipmentException;
import com.mariluz.shipping.exceptions.CouldNotUpdateShipmentException;
import com.mariluz.shipping.exceptions.ShipmentNotFoundException;
import com.mariluz.shipping.exceptions.UnauthenticatedException;
import com.mariluz.shipping.exceptions.UnauthorizedOperationException;
import com.mariluz.shipping.mapper.ShipmentMapper;
import com.mariluz.shipping.model.Shipment;
import com.mariluz.shipping.model.Status;
import com.mariluz.shipping.model.User;
import com.mariluz.shipping.repository.ShippingRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

    private final ShippingRepository repo;

    private final ShipmentMapper mapper;

    // ------------------ Helpers privados para validar rol usuario -------------------

    private User getCurrentUser() {
        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new UnauthenticatedException("No hay un usuario autenticado");
        }
        return user;
    }

    private void validateAdminAccess(String message) {
        User user = getCurrentUser();

        if (!user.getRole().equalsIgnoreCase("ADMIN")) {
            // si el usuario no es admin arrojamos un error
            throw new UnauthorizedOperationException(message);
        }
    }

    // ------------------ Implementacion metodos service -------------------

    /*
        Faltaba validar que no exista un envio vigente para el SaleId
        -> si el envio esta en Status.CANCELED lo podemos crear
    */
    @Override
    public ShipmentResponse createShipment(CreateShipmentRequest request) {
        // 1. obtener usuario actual
        User user = getCurrentUser();

        // 2. validar que no exista un envio para esta venta aun
        Optional<Shipment> shipmentOpt = repo.findBySaleIdAndUserId(
            request.getSaleId(),
            user.getId()
        );
        if (
            shipmentOpt.isPresent() &&
            !shipmentOpt.get().getStatus().equals(Status.CANCELLED)
        ) {
            throw new CouldNotCreateShipmentException(
                "Ya existe un envío para esta venta"
            );
        }

        // 3. crear o reactivar envío según si existe uno cancelado
        Shipment shipment;
        if (shipmentOpt.isPresent()) {
            // reactivar el envío cancelado con la nueva dirección
            shipment = shipmentOpt.get();
            mapper.reactivateFromRequest(shipment, request);
        } else {
            shipment = mapper.toEntity(request, user.getId());
        }

        // 4. guardar y devolver respuesta
        return mapper.toResponse(repo.save(shipment));
    }

    @Override
    public ShipmentResponse updateShipmentStatus(
        UpdateShipmentStatusRequest request
    ) {
        // 1. validar acceso de admin
        validateAdminAccess(
            "Solo los administradores pueden actualizar el estado de un envío"
        );

        // 2. obtener y validar envío
        Shipment shipment = repo
            .findById(request.getShipmentId())
            .orElseThrow(ShipmentNotFoundException::new);

        // 3. validar que el envío no esté en estado terminal
        if (
            shipment.getStatus() == Status.DELIVERED ||
            shipment.getStatus() == Status.CANCELLED
        ) {
            throw new CouldNotUpdateShipmentException(
                "No se puede modificar un envío en estado " +
                    shipment.getStatus()
            );
        }

        // 4. mapear estado y retornar entidad guardada
        mapper.updateStatus(shipment, request);
        return mapper.toResponse(repo.save(shipment));
    }

    @Override
    public MyShipmentsResponse getMyShipments() {
        // 1. obtener usuario actual
        User user = getCurrentUser();
        // 2. obtener y devolver envios del usuario
        List<Shipment> shipments = repo.findByUserId(user.getId());
        return mapper.toMyShipmentsResponse(shipments);
    }

    @Override
    public void cancelShipment(Integer saleId) {
        // 1. obtener y validar envío
        Shipment shipment = repo
            .findBySaleIdAndUserId(saleId, getCurrentUser().getId())
            .orElseThrow(ShipmentNotFoundException::new);

        // 2. validar que el envío no esté en estado terminal
        if (
            shipment.getStatus() == Status.DELIVERED ||
            shipment.getStatus() == Status.CANCELLED
        ) {
            throw new CouldNotCancelShipmentException(
                "No se puede cancelar un envío en estado " +
                    shipment.getStatus()
            );
        }

        // 3. cancelar envío
        shipment.setStatus(Status.CANCELLED);
        repo.save(shipment);
    }

    @Override
    public ShipmentsResponse getShipments() {
        // 1. validar admin
        validateAdminAccess(
            "Solo los administradores pueden listar los envíos"
        );
        // 2. obtener y devolver envios
        List<Shipment> shipments = repo.findAll();
        return mapper.toShipmentsResponse(shipments);
    }
}
