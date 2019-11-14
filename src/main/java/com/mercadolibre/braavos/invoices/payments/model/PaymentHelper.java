package com.mercadolibre.braavos.invoices.payments.model;

import com.mercadolibre.braavos.invoices.CurrencyType;
import com.mercadolibre.braavos.invoices.ConversionFactor;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PaymentHelper {
    String generateId = Instant.now().toString() + UUID.randomUUID();
    String userId;
    CurrencyType currencyType;
    Double amount;
    Option<ConversionFactor> conversionFactor;

    public Double getEffectiveAmount() {
        return conversionFactor.map(c -> c.getValue() * amount).getOrElse(amount);
    }

    public PaymentHelper withAmountAndCurrencyType(Double amount, CurrencyType currencyType, Option<ConversionFactor> conversionFactor) {
        return toBuilder()
                .amount(amount)
                .currencyType(currencyType)
                .conversionFactor(conversionFactor)
                .build();
    }
}
