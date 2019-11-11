package com.mercadolibre.braavos.invoices.payments;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentApi {
    @NotNull
    String userId;
    @NotNull
    String currency;
    @NotNull
    Double amount;
}
