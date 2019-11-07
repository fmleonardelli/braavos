package com.mercadolibre.invoices.model;

import io.vavr.collection.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Charge {
    Instant date;
    String eventId;
    String eventType;
    Double amount;
    String currency;
    List<Payment> payments;
}
