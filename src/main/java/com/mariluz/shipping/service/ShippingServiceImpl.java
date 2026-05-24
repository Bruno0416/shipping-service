package com.mariluz.shipping.service;

import com.mariluz.shipping.dto.*;
import com.mariluz.shipping.exceptions.CouldNotCancelShipmentException;
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

    @Override
    public ShipmentResponse createShipment(CreateShipmentRequest request) {
        // 1. obtener usuario actual
        User user = getCurrentUser();
        // 2. crear y guardar objeto
        Shipment shipment = repo.save(mapper.toEntity(request, user.getId()));

        // 3. devolver respuesta
        return mapper.toResponse(shipment);
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
