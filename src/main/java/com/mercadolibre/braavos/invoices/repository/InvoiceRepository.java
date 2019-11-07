package com.mercadolibre.braavos.invoices.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.invoices.model.Invoice;
import com.mongodb.MongoClient;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.mongojack.JacksonMongoCollection;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InvoiceRepository {

    JacksonMongoCollection<Invoice> collection;
    ObjectMapper objectMapper;

     public InvoiceRepository(MongoClient client, String databaseName, String collectionName, ObjectMapper mapper) {
         val mongoCollection = client.getDatabase(databaseName).getCollection(collectionName);
         JacksonMongoCollection.JacksonMongoCollectionBuilder<Invoice> builder = JacksonMongoCollection.builder();
         this.collection = builder.withObjectMapper(mapper).build(mongoCollection, Invoice.class);
         this.objectMapper = mapper;
     }

    private Either<Throwable, Invoice> insert(Invoice notification) {
        try {
            collection.insert(notification);
            return Either.right(notification);
        } catch (Exception ex) {
            return Either.left(ex);
        }
    }

    public Either<Throwable, Invoice> save(Invoice notification) {
         return insert(notification);
    }
}
