package com.mercadolibre.braavos.invoices.api.error;

import lombok.Value;

@Value
public class DataNotFound extends Exception {

    public DataNotFound(String message) {
        super(message);
    }
}
