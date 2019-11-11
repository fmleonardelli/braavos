package com.mercadolibre.braavos.invoices.repository;

@FunctionalInterface
public interface RepositoryUpdate<T> {

    T findAndModify();
}
