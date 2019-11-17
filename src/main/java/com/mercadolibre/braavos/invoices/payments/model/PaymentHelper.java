package com.mercadolibre.braavos.invoices.payments.model;

import com.mercadolibre.braavos.invoices.ConversionFactor;
import com.mercadolibre.braavos.invoices.CurrencyType;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PaymentHelper {
    String generateId = UUID.randomUUID().toString();
    String userId;
    CurrencyType currencyType;
    BigDecimal amount;
    Option<ConversionFactor> conversionFactor;

    public BigDecimal getEffectiveAmount() {
        return conversionFactor.map(c -> c.getValue().multiply(amount)).getOrElse(amount);
    }

    public PaymentHelper withAmountAndCurrencyType(BigDecimal amount, CurrencyType currencyType, Option<ConversionFactor> conversionFactor) {
        return toBuilder()
                .amount(amount)
                .currencyType(currencyType)
                .conversionFactor(conversionFactor)
                .build();
    }
}
