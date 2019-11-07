package com.mercadolibre.invoices.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Invoice {
    String userId;
    Instant periodDate;
    List<Charge> charges;
}
