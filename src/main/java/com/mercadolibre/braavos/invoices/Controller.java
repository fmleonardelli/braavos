package com.mercadolibre.braavos.invoices;

import com.mercadolibre.braavos.invoices.api.Paginated;
import com.mercadolibre.braavos.invoices.api.error.ApiError;
import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

public interface Controller<T> {
    default ResponseEntity<T> convertToResponse(Either<Throwable, T> res) throws Throwable {
        if (res.isRight()) {
            return new ResponseEntity<>(res.get(), HttpStatus.OK);
        } else {
            throw res.getLeft();
        }
    }
    default ResponseEntity<Paginated<T>> convertToResponsePaginated(Either<Throwable, Paginated<T>> res) {
        return res.map(t -> new ResponseEntity<>(t, HttpStatus.OK)).getOrElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }
    default ResponseEntity<Object> handleLeft(Throwable error) {
        return Match(error).of(
                Case($(instanceOf(Throwable.class)),
                        new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST.toString(), error.getMessage()), HttpStatus.BAD_REQUEST)),
                Case($(),
                        new ResponseEntity<>(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), error.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR)));
    }
}
