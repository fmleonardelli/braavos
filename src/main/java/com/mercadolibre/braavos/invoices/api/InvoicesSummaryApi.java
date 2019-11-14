package com.mercadolibre.braavos.invoices.api;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class InvoicesSummaryApi {
    String userId;
    Double totalCharges;
    Double totalPayments;
    Double total;
}
