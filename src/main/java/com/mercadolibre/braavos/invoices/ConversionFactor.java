package com.mercadolibre.braavos.invoices;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ConversionFactor {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate date;
    BigDecimal value;
}
