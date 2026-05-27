package com.mariluz.shipping.repository;

import com.mariluz.shipping.model.Shipment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRepository extends JpaRepository<Shipment, Integer> {
    List<Shipment> findByUserId(String userId);

    Optional<Shipment> findBySaleIdAndUserId(Integer saleId, String userId);
}
