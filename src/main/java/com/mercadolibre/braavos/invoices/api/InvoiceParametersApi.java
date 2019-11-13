package com.mercadolibre.braavos.invoices.api;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class InvoiceParametersApi {
    Option<String> userId;
    Option<String> periodDate;
    Option<Integer> limit;
    Option<Integer> offset;
}
