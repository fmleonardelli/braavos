package com.mercadolibre.braavos.invoices.repository;

import io.vavr.control.Option;

public interface RepositoryParameters {
    Option<Integer> limitParam();
    Option<Integer> offSetParam();
    default Integer limit() {
        return limitParam().getOrElse(100);
    }
    default Integer offset() {
        return offSetParam().getOrElse(0);
    }

}
