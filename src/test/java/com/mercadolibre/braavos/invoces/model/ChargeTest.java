package com.mercadolibre.braavos.invoces.model;

import com.mercadolibre.braavos.invoices.charges.Charge;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

public class ChargeTest {

    private static Logger logger = LoggerFactory.getLogger(ChargeTest.class);

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
}
