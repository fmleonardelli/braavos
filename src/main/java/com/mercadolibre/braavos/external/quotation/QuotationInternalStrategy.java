package com.mercadolibre.braavos.external.quotation;

import com.mercadolibre.braavos.invoices.model.ConversionFactor;
import com.mercadolibre.braavos.invoices.model.CurrencyType;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;

import static io.vavr.API.Right;

@Service
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QuotationInternalStrategy implements QuotationSource {
    public Either<Throwable, ConversionFactor> getQuotationByTypeAndDate(CurrencyType type, Instant date) {
        return Right(new ConversionFactor(date.atZone(ZoneId.of("UTC")).toLocalDate(), new BigDecimal(60.3 ).setScale(2, BigDecimal.ROUND_HALF_UP)));
    }
    public Integer order() {
        return 1;
    }
}
