package com.mercadolibre.braavos.invoices.repository;

@FunctionalInterface
public interface RepositoryFind<T> {
    Iterable<T> find();
}
