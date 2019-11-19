package com.mercadolibre.braavos.invoices.payments.model;

import com.mercadolibre.braavos.invoices.model.ConversionFactor;
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
    /**
     * Payment Date
     */
    Instant date;
    /**
     * Amount in default currency
     */
    BigDecimal amount;
    /**
     * Entered Currency
     */
    String currency;
    /**
     * Entered Amount
     */
    BigDecimal originalAmount;
    /**
     * Currency Value for conversion
     */
    Option<ConversionFactor> conversionFactor;
}
