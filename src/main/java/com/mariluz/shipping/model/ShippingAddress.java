package com.mariluz.shipping.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable // permite agrupar datos relacionados en una sola entidad manteniendo el orden
@Builder
@Data
@NoArgsConstructor  // requerido por Hibernate para instanciar @Embeddable
@AllArgsConstructor // requerido por @Builder cuando existe @NoArgsConstructor
public class ShippingAddress {

    @Column(nullable = false, length = 80)
    private String region;

    @Column(nullable = false, length = 60)
    private String commune;

    @Column(nullable = false, length = 100)
    private String street;

    @Column(nullable = false, length = 10)
    private String number;

    @Column(length = 100)
    private String reference;
}
