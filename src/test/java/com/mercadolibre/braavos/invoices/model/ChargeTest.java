package com.mercadolibre.braavos.invoices.model;

import com.mercadolibre.braavos.invoices.charges.Charge;
import com.mercadolibre.braavos.invoices.charges.ChargeState;
import com.mercadolibre.braavos.invoices.payments.model.Payment;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

public class ChargeTest {

    @Test
    public void checkChargeProperties() {
        Charge charge = Charge
                .builder()
                .eventId("")
                .eventType("")
                .amount(BigDecimal.ZERO)
                .currency("")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(Option.none())
                .payments(List.empty())
                .build();

        assertThat(charge, hasProperty("eventId"));
        assertThat(charge, hasProperty("eventType"));
        assertThat(charge, hasProperty("amount"));
        assertThat(charge, hasProperty("currency"));
        assertThat(charge, hasProperty("processedDate"));
        assertThat(charge, hasProperty("date"));
        assertThat(charge, hasProperty("conversionFactor"));
        assertThat(charge, hasProperty("processedDate"));
        assertThat(charge, hasProperty("payments"));
    }

    @Test
    public void statusPending() {
        Payment payment = new Payment("id",
                Instant.now(),
                new BigDecimal(25),
                "USD",
                new BigDecimal(25),
                Option.of(new ConversionFactor(LocalDate.now(), new BigDecimal(60.32))));
        Charge charge = Charge
                .builder()
                .eventId("")
                .eventType("")
                .amount(new BigDecimal(58.60))
                .currency("USD")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(Option.none())
                .payments(List.of(payment))
                .build();

        assert charge.status().equals(ChargeState.PENDING.getDescription());
    }

    @Test
    public void statusPendingWithManyPayments() {
        Payment payment = new Payment("id",
                Instant.now(),
                new BigDecimal(10),
                "USD",
                new BigDecimal(25),
                Option.of(new ConversionFactor(LocalDate.now(), new BigDecimal(60.32))));
        Payment payment2 = new Payment("id",
                Instant.now(),
                new BigDecimal(20),
                "ARS",
                new BigDecimal(25),
                Option.of(new ConversionFactor(LocalDate.now(), new BigDecimal(60.32))));
        Charge charge = Charge
                .builder()
                .eventId("")
                .eventType("")
                .amount(new BigDecimal(58.60))
                .currency("USD")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(Option.none())
                .payments(List.of(payment))
                .build();

        assert charge.status().equals(ChargeState.PENDING.getDescription());
    }

    @Test
    public void statusCompleted() {
        val conversionFactor = Option.of(new ConversionFactor(LocalDate.now(), new BigDecimal(60.32)));
        val amount = new BigDecimal(58.60);
        Payment payment = new Payment("id",
                Instant.now(),
                conversionFactor.map(c -> amount.multiply(c.getValue())).getOrElse(amount).setScale(2, BigDecimal.ROUND_HALF_UP),
                "USD",
                new BigDecimal(58.60),
                conversionFactor);
        Charge charge = Charge
                .builder()
                .eventId("")
                .eventType("")
                .amount(new BigDecimal(58.60))
                .currency("USD")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(conversionFactor)
                .payments(List.of(payment))
                .build();

        assert charge.status().equals(ChargeState.COMPLETED.getDescription());
    }
}
