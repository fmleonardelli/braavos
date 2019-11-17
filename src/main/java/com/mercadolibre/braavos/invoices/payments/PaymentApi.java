package com.mercadolibre.braavos.invoices.payments;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mercadolibre.braavos.invoices.ConversionFactor;
import com.mercadolibre.braavos.invoices.payments.model.Payment;
import io.vavr.Function1;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentApi {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    Instant date;
    BigDecimal amountInCharge;
    String currency;
    BigDecimal originalAmount;
    Option<ConversionFactor> conversionFactor;

    public static Function1<Payment, PaymentApi> map() {
        return x -> new PaymentApi(
                x.getDate(),
                x.getAmount(),
                x.getCurrency(),
                x.getOriginalAmount(),
                x.getConversionFactor()
        );
    }
}
