package com.mercadolibre.braavos.invoices.repo.functions;

@FunctionalInterface
public interface RepositoryFindOne<T> {
    T findOne();
}
