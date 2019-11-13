package com.mercadolibre.braavos.invoices.payments;

import com.mercadolibre.braavos.invoices.ConversionFactor;
import com.mercadolibre.braavos.invoices.payments.model.Payment;
import io.vavr.Function1;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentApi {
    Double amountInCharge;
    String currency;
    Double originalAmount;
    Option<ConversionFactor> conversionFactor;

    public static Function1<Payment, PaymentApi> map() {
        return x -> new PaymentApi(
            x.getAmount(),
            x.getCurrency(),
                x.getOriginalAmount(),
                x.getConversionFactor()
        );
    }
}
