package com.mercadolibre.braavos.invoices.payments.model;

import com.mercadolibre.braavos.invoices.model.ConversionFactor;
import com.mercadolibre.braavos.invoices.model.CurrencyType;
import io.vavr.control.Option;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Value
public class PaymentHelper {
    String generateId;
    String userId;
    CurrencyType currencyType;
    BigDecimal amount;
    Option<ConversionFactor> conversionFactor;
    BigDecimal originalAmount;

    public PaymentHelper(String userId, CurrencyType currencyType, BigDecimal amount, Option<ConversionFactor> conversionFactor) {
        this.generateId = UUID.randomUUID().toString();
        this.userId = userId;
        this.currencyType = currencyType;
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.conversionFactor = conversionFactor;
        this.originalAmount = this.amount;
    }

    PaymentHelper(String generateId, String userId, CurrencyType currencyType, BigDecimal amount, Option<ConversionFactor> conversionFactor, BigDecimal originalAmount) {
        this.generateId = generateId;
        this.userId = userId;
        this.currencyType = currencyType;
        this.amount = amount;
        this.conversionFactor = conversionFactor;
        this.originalAmount = originalAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public PaymentHelper withAmount(BigDecimal amount) {
        return new PaymentHelper(
                this.generateId,
                this.userId,
                this.currencyType,
                this.getNewAmount(amount),
                this.conversionFactor,
                this.originalAmount);
    }

    public BigDecimal getEffectiveAmount() {
        return conversionFactor.map(c -> c.getValue().multiply(amount))
                .getOrElse(amount)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    BigDecimal getNewAmount(BigDecimal newAmount) {
        return conversionFactor.map(c ->  newAmount.divide(c.getValue()))
                .getOrElse(newAmount)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
