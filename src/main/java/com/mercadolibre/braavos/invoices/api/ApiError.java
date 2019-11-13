package com.mercadolibre.braavos.invoices.api;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ApiError {
    String code;
    String message;
}
