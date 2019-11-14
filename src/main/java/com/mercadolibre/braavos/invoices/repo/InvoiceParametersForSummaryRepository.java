package com.mercadolibre.braavos.invoices.repo;

import com.mercadolibre.braavos.invoices.api.InvoiceParametersForSummaryApi;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Value;

import static io.vavr.API.Tuple;

@Value
@AllArgsConstructor
public class InvoiceParametersForSummaryRepository implements ParametersRepository {
    InvoiceParametersForSummaryApi parametersApi;

    public Option<Integer> limitParam() {
        return Option.none();
    }

    public Option<Integer> offsetParam() {
        return Option.none();
    }

    public Map<String, Object> toMapForRepo() {
        return List.of(
                Option.of(Tuple("userId", parametersApi.getUserId())),
                filterByDates(parametersApi.periodFromDate(), parametersApi.periodToDate())
        ).flatMap(x -> x.map(r -> r)).toMap(x -> x);
    }
}
