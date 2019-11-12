package com.mercadolibre.braavos.invoices.repo.functions;

@FunctionalInterface
public interface RepositoryUpdate<T> {

    T findAndModify();
}
