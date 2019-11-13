package com.mercadolibre.braavos.invoices.repo.ops;

@FunctionalInterface
public interface RepositoryUpdate<T> {

    T findAndModify();
}
