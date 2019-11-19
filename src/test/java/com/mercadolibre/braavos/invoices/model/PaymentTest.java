package com.mercadolibre.braavos.invoices.model;

import com.mercadolibre.braavos.invoices.payments.model.Payment;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

public class PaymentTest {

    @Test
    public void checkPaymentProperties() {
        Payment payment = Payment
                .builder()
                .id("id")
                .date(Instant.now())
                .amount(BigDecimal.ZERO)
                .currency("ARS")
                .originalAmount(BigDecimal.ZERO)
                .conversionFactor(Option.none())
                .build();
        assertThat(payment, hasProperty("id"));
        assertThat(payment, hasProperty("date"));
        assertThat(payment, hasProperty("amount"));
        assertThat(payment, hasProperty("currency"));
        assertThat(payment, hasProperty("originalAmount"));
        assertThat(payment, hasProperty("conversionFactor"));
    }
}
