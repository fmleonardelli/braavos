package com.mercadolibre.braavos.external.quotation;

import com.mercadolibre.braavos.invoices.model.CurrencyType;
import com.mercadolibre.braavos.invoices.model.ConversionFactor;
import io.vavr.control.Either;

import java.time.Instant;

public interface QuotationSource {
    Either<Throwable, ConversionFactor> getQuotationByTypeAndDate(CurrencyType type, Instant date);

    Integer order();
}
