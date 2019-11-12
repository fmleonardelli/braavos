package com.mercadolibre.braavos.invoices.repo.functions;

@FunctionalInterface
public interface RepositoryFind<T> {
    Iterable<T> find();
}
