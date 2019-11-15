package com.mercadolibre.braavos.invoices.repo;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.bson.Document;

import java.time.Instant;

import static io.vavr.API.Tuple;

public interface ParametersRepository {
    Option<Integer> limitParam();
    Option<Integer> offsetParam();
    Map<String, Object> toMapForRepo();
    default Integer limit() {
        return limitParam().getOrElse(50);
    }
    default Integer offset() {
        return offsetParam().getOrElse(0);
    }

    default Option<Tuple2<String, Object>> filterByDates(String field, Option<Instant> from, Option<Instant> to) {
        if (!from.isEmpty() && !to.isEmpty()) {
            Map<String, Object> query = HashMap.of("$gte", from, "$lte", to);
            return Option.of(Tuple(field, new Document(query.toJavaMap())));
        } else {
            if (!from.isEmpty()) {
                Map<String, Object> query = HashMap.of("$gte", from);
                return Option.of(Tuple(field, new Document(query.toJavaMap())));
            } else {
                if (!to.isEmpty()) {
                    Map<String, Object> query = HashMap.of("$lte", to);
                    return Option.of(Tuple(field, new Document(query.toJavaMap())));
                }
            }
        }
        return Option.none();
    }
}
