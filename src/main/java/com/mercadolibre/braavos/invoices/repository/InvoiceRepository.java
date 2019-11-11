package com.mercadolibre.braavos.invoices.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.braavos.config.Repository;
import com.mercadolibre.braavos.invoices.Invoice;
import com.mongodb.MongoClient;
import com.mongodb.client.model.IndexOptions;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Traversable;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.bson.Document;
import org.mongojack.JacksonMongoCollection;

import java.time.Instant;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InvoiceRepository extends Repository<Invoice> {

    ObjectMapper objectMapper;

     public InvoiceRepository(MongoClient client, String databaseName, String collectionName, ObjectMapper mapper) {
         val mongoCollection = client.getDatabase(databaseName).getCollection(collectionName);
         JacksonMongoCollection.JacksonMongoCollectionBuilder<Invoice> builder = JacksonMongoCollection.builder();
         this.collection = builder.withObjectMapper(mapper).build(mongoCollection, Invoice.class);
         this.objectMapper = mapper;

         Map<String, Object> index = HashMap.of("userId", 1, "periodDate", 1, "charges.eventId", 1, "charges.eventType", 1);
         this.collection.createIndex(new Document(index.toJavaMap()), new IndexOptions().unique(true));
         this.collection.createIndex(new Document("charges.payments.date", 1));
         this.collection.createIndex(new Document("charges.payments.id", 1));
     }

    public Either<Throwable, Option<Invoice>> getInvoiceByUserIdAndPeriod(String userId, Instant periodDate) {
        Map<String, Object> query = HashMap.of("userId", userId, "periodDate", periodDate);
        RepositoryFindOne<Invoice> repo = () -> this.collection.findOne(new Document(query.toJavaMap()));
        return Try.of(repo ::findOne).toEither().map(Option::of);
    }

    public Either<Throwable, Option<Invoice>> getInvoicesByUserIdAndChargesState(String userId, String chargeState) {
        val elemMatch = new Document("$elemMatch", new Document("status", chargeState));
        Map<String, Object> query = HashMap.of("userId", userId, "charges", elemMatch);
        RepositoryFind<Invoice> repo = () -> collection.find(new Document(query.toJavaMap())).sort(new Document("periodDate", 1)).limit(1);
        return Try.of(repo :: find).toEither().map(List::ofAll).map(Traversable::singleOption);
    }

    public Either<Throwable, Invoice> save(Invoice invoice) {
         return insert(invoice);
    }

    public Either<Throwable, Invoice> update(Invoice invoice) {
        val document = JacksonMongoCollection.convertToDocument(invoice, this.objectMapper, Invoice.class);
        Map<String, Object> query = HashMap.of("userId", invoice.getUserId(), "periodDate", invoice.getPeriodDate());
        RepositoryUpdate<Invoice> repo = () -> collection.findAndModify(new Document(query.toJavaMap()),
                new Document(),
                new Document(),
                collection.serializeFields(incVersion(document)),
                true,
                false);
        return Try.of(repo :: findAndModify).toEither();
    }
}
