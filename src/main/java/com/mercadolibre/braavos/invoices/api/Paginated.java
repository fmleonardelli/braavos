package com.mercadolibre.braavos.invoices.api;

import io.vavr.collection.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Paginated<T> {
    List<T> items;
    Integer offset;
    Integer limit;
    Long total;
}
