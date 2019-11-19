package com.mercadolibre.braavos.invoices.api;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@AllArgsConstructor
public class InvoicesSummaryApi {
    String userId;
    BigDecimal totalCharges;
    BigDecimal totalPayments;
    BigDecimal debt;
}
