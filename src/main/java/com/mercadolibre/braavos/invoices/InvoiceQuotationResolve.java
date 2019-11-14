package com.mercadolibre.braavos.invoices;

import com.mercadolibre.braavos.external.quotation.QuotationSource;
import io.vavr.API;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceQuotationResolve {

    @Autowired
    List<QuotationSource> quotationSources;

    public Either<Throwable, Option<ConversionFactor>> getQuotationByTypeAndDate(CurrencyType type, Instant date) {
        if (type.getIsDefault()) {
            return Right(Option.none());
        } else {
            val quotationStrategies = io.vavr.collection.List.ofAll(quotationSources).sortBy(QuotationSource::order);
            return execute(quotationStrategies, type, date).map(API::Option);
        }
    }

    Either<Throwable, ConversionFactor> execute(io.vavr.collection.List<QuotationSource> quotationStrategies, CurrencyType type, Instant date) {
        if (quotationStrategies.isEmpty()) {
            return Left(new RuntimeException("The quotation could not be obtained"));
        } else {
            val executionRes = quotationStrategies.head().getQuotationByTypeAndDate(type, date);
            if (executionRes.isRight()) return executionRes;
            else return execute(quotationStrategies.tail(), type, date);
        }
    }
}
