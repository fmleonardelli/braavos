package com.mercadolibre.braavos.invoices.api.error;

import lombok.Value;

@Value
public class ValidationError extends Exception {

    public ValidationError(String message) {
        super(message);
    }
}
