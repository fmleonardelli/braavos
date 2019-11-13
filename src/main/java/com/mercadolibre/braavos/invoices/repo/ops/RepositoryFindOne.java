package com.mercadolibre.braavos.invoices.repo.ops;

@FunctionalInterface
public interface RepositoryFindOne<T> {
    T findOne();
}
