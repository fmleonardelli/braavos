package com.mercadolibre.braavos.invoices.charges;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadolibre.braavos.invoices.kafka.EventNotification;
import com.mercadolibre.braavos.invoices.ConversionFactor;
import com.mercadolibre.braavos.invoices.payments.model.Payment;
import com.mercadolibre.braavos.invoices.payments.model.PaymentHelper;
import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.*;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Charge {
    String eventId;
    String eventType;
    Double amount;
    String currency;
    Instant processedDate;
    Instant date;
    Option<ConversionFactor> conversionFactor;
    List<Payment> payments;

    public static Function2<EventNotification, Option<ConversionFactor>, Charge> map() {
        return (x, y) -> new Charge(
                x.getEventId(),
                x.getEventType(),
                x.getAmount(),
                x.getCurrency(),
                Instant.now(),
                x.getDate(),
                y,
                List.of());
    }

    @JsonProperty
    public String status() {
        if (Double.compare(differenceToComplete(), 0d) == 1) {
            return ChargeState.PENDING.getDescription();
        }
        return ChargeState.COMPLETED.getDescription();
    }

    /**
     *
     * @return difference between the charge amount and sum of payments
     */
    public Double differenceToComplete() {
        val effectiveAmountCharge = conversionFactor.map(c-> c.getValue() * this.amount).getOrElse(this.amount);
        return payments.foldLeft(effectiveAmountCharge, (seed, elem) -> seed - elem.getAmount());
    }

    public Charge addPayment(Double amount, PaymentHelper paymentHelper) {
        val newPayment = new Payment(
                paymentHelper.getGenerateId(),
                Instant.now(),
                amount,
                paymentHelper.getCurrencyType().getIdentifier(),
                paymentHelper.getAmount(),
                paymentHelper.getConversionFactor());
        return toBuilder().payments(payments.append(newPayment)).build();
    }
}
