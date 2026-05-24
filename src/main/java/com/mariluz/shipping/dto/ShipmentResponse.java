package com.mariluz.shipping.dto;

import com.mariluz.shipping.model.Status;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ShipmentResponse {

    private Integer id;

    private Integer saleId;

    private String userId;

    private Status status;

    private ShippingAddressResponse address;
}
