package com.mercadolibre.braavos.invoices.repo;

import io.vavr.collection.Map;
import io.vavr.control.Option;

public interface ParametersRepository {
    Option<Integer> limitParam();
    Option<Integer> offsetParam();
    Map<String, Object> toMapForRepo();
    default Integer limit() {
        return limitParam().getOrElse(50);
    }
    default Integer offset() {
        return offsetParam().getOrElse(0);
    }
}
