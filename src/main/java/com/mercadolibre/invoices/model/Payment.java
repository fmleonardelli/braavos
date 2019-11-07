package com.mercadolibre.invoices.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Payment {
    Instant date;
    String currency;
    Double amount;
}
