package com.mercadolibre.braavos.invoices.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mercadolibre.braavos.invoices.Invoice;
import com.mercadolibre.braavos.invoices.charges.ChargeApi;
import io.vavr.Function1;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

@Value
@AllArgsConstructor
public class InvoiceApi {
    String userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate periodDate;
    List<ChargeApi> charges;

    public static Function1<Invoice, InvoiceApi> map() {
        return x -> new InvoiceApi(
                x.getUserId(),
                x.getPeriodDate(),
                x.getCharges().map(c -> ChargeApi.map().apply(c))
        );
    }
}
