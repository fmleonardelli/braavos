package com.mercadolibre.braavos.invoices;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

@Value
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ConversionFactor {
    LocalDate date;
    Double value;
}
