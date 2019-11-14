package com.mercadolibre.braavos.invoices.api;

import io.vavr.control.Either;

import java.time.Instant;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

public interface ParametersApi {

    default Either<Throwable, Instant> convertStrToDate(String dateStr, String timeStr) {
        try {
            return Right(Instant.parse(dateStr.trim() + timeStr.trim()));
        } catch (Exception ex) {
            return Left(new Throwable("The accepted date format is: yyyy-MM-dd"));
        }
    }

    default Either<Throwable, Instant> from(String fromStr) {
        return convertStrToDate(fromStr, "T00:00:00.000Z");
    }

    default Either<Throwable, Instant> to(String toStr) {
        return convertStrToDate(toStr, "T23:59:59.999Z");
    }
}
