package com.mercadolibre.braavos.invoices.repository;

@FunctionalInterface
public interface RepositoryFindOne<T> {
    T findOne();
}
