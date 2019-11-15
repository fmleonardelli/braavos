package com.mercadolibre.braavos.invoices.repo;

import com.mercadolibre.braavos.invoices.api.InvoiceParametersApi;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Value;

import static io.vavr.API.Tuple;

@Value
@AllArgsConstructor
public class InvoiceParametersRepository implements ParametersRepository {

    InvoiceParametersApi parametersApi;
    public Option<Integer> limitParam() {
        return parametersApi.getLimit();
    }
    public Option<Integer> offsetParam() {
        return parametersApi.getOffset();
    }

    public Map<String, Object> toMapForRepo() {
        return List.of(
                parametersApi.getUserId().map(u -> Tuple("userId", u)),
                filterByDates("periodDate", parametersApi.periodFromDate(), parametersApi.periodToDate())
        ).flatMap(x -> x.map(r -> r)).toMap(x -> x);
    }
}
