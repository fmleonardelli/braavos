package com.mercadolibre.braavos.invoices.charges;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ChargeState {
    PENDING("ADEUDA"),
    COMPLETED("PAGADO"),
    EXCESS("EXCEDENTE");

    String description;
}
