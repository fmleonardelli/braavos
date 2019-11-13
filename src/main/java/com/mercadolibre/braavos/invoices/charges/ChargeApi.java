package com.mercadolibre.braavos.invoices.charges;

import com.mercadolibre.braavos.invoices.ConversionFactor;
import com.mercadolibre.braavos.invoices.payments.PaymentApi;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChargeApi {
    String eventId;
    String eventType;
    Double amount;
    String currency;
    Instant date;
    Option<ConversionFactor> conversionFactor;
    List<PaymentApi> payments;

    public static Function1<Charge, ChargeApi> map() {
        return x -> new ChargeApi(
                x.getEventId(),
                x.getEventType(),
                x.getAmount(),
                x.getCurrency(),
                x.getDate(),
                x.getConversionFactor(),
                x.getPayments().map(p -> PaymentApi.map().apply(p))
        );
    }
}
