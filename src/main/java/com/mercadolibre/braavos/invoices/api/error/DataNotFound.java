package com.mercadolibre.braavos.invoices.api.error;

public class DataNotFound extends Exception {

    public DataNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotFound(String message) {
        super(message);
    }
}
