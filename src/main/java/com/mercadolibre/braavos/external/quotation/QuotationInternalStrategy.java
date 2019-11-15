package com.mercadolibre.braavos.external.quotation;

import com.mercadolibre.braavos.invoices.ConversionFactor;
import com.mercadolibre.braavos.invoices.CurrencyType;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;

import static io.vavr.API.Right;

@Service
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QuotationInternalStrategy implements QuotationSource {
    public Either<Throwable, ConversionFactor> getQuotationByTypeAndDate(CurrencyType type, Instant date) {
        return Right(new ConversionFactor(date.atZone(ZoneId.of("UTC")).toLocalDate(), 60.3 ));
    }
    public Integer order() {
        return 1;
    }
}
