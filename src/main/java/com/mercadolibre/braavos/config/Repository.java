package com.mercadolibre.braavos.config;

import io.vavr.control.Either;
import lombok.val;
import org.bson.Document;
import org.mongojack.JacksonMongoCollection;

public abstract class Repository<T> {

    protected JacksonMongoCollection<T> collection;

    protected Either<Throwable, T> insert(T entity) {
        try {
            collection.insert(entity);
            return Either.right(entity);
        } catch (Exception ex) {
            return Either.left(ex);
        }
    }

    protected Document incVersion(Document document) {
        document.remove("version");
        val update = new Document("$set", document);
        update.put("$inc", new Document("version", 1));
        return update;
    }

}
