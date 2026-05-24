package com.mariluz.shipping.dto;

import java.util.List;
import lombok.Data;

@Data
public class MyShipmentsResponse {

    private List<ShipmentResponse> shipments;
}
