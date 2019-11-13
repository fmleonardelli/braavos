package com.mercadolibre.braavos.invoices.repo.ops;

@FunctionalInterface
public interface RepositoryFind<T> {
    Iterable<T> find();
}
