package com.mercadolibre.braavos.invoices.payments.model;

import com.mercadolibre.braavos.invoices.ConversionFactor;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class Payment {
    String id;
    Instant date;
    BigDecimal amount;
    String currency;
    BigDecimal originalAmount;
    Option<ConversionFactor> conversionFactor;
}
