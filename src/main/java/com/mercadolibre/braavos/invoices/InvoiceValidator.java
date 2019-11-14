package com.mercadolibre.braavos.invoices;

import com.mercadolibre.braavos.invoices.api.error.ValidationError;
import com.mercadolibre.braavos.invoices.charges.ChargeType;
import io.vavr.collection.List;
import io.vavr.control.Either;

public interface InvoiceValidator {
    default Either<Throwable, Boolean> checkType(String type) {
        if (!List.of(ChargeType.values()).flatMap(ChargeType::getTypes).exists(t -> t.equalsIgnoreCase(type))){
            return Either.left(new ValidationError("Charge type not supported: " + type));
        }
        return Either.right(true);
    }
    default Either<Throwable, CurrencyType> checkCurrency(String currency) {
        return List.of(CurrencyType.values())
                .find(c -> c.getIdentifier().equals(currency))
                .toEither(new ValidationError("The currencyType is invalid: " + currency));
    }
    default Either<Throwable, Boolean> checkAmount(Double amount) {
        if (Double.compare(amount, 0d) != 1) {
            return Either.left(new ValidationError("Invalid amount: " + amount));
        }
        return Either.right(true);
    }
}
