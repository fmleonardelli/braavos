package com.mercadolibre.braavos.invoices.payments;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentInputApi {
    @NotNull
    String userId;
    @NotNull
    String currency;
    @NotNull
    BigDecimal amount;
}
