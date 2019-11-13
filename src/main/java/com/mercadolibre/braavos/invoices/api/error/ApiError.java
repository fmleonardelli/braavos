package com.mercadolibre.braavos.invoices.api.error;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ApiError {
    String code;
    String message;
}
