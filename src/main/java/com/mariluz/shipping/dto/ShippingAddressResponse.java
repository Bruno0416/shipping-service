package com.mariluz.shipping.dto;

import lombok.Data;

@Data
public class ShippingAddressResponse {

    private String region;

    private String commune;

    private String street;

    private String number;

    private String reference;
}
