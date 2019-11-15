package com.mercadolibre.braavos.invoices.api;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;

@Value
@AllArgsConstructor
public class InvoiceParametersApi implements ParametersApi {
    Option<String> userId;
    Option<String> periodDateFrom;
    Option<String> periodDateTo;
    Option<Integer> limit;
    Option<Integer> offset;

    public Option<Instant> periodFromDate() {
        return periodDateFrom.map(f -> from(f).getOrElse(Instant.now()));
    }

    public Option<Instant> periodToDate() {
        return periodDateTo.map(t -> to(t).getOrElse(Instant.now()));
    }
}
