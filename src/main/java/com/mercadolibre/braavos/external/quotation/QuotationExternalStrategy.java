package com.mercadolibre.braavos.external.quotation;

import com.mercadolibre.braavos.invoices.CurrencyType;
import com.mercadolibre.braavos.invoices.ConversionFactor;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static io.vavr.API.Left;

@Service
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QuotationExternalStrategy implements QuotationSource {
    public Either<Throwable, ConversionFactor> getQuotationByTypeAndDate(CurrencyType type, Instant date) {
        return Left(new RuntimeException("Source not implemented"));
    }
    public Integer order() {
        return 0;
    }
}
