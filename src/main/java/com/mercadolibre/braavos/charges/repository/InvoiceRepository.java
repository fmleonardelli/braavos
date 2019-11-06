package com.mercadolibre.braavos.charges.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.braavos.charges.kafka.ChargeNotification;
import com.mongodb.MongoClient;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.mongojack.JacksonMongoCollection;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InvoiceRepository {

    JacksonMongoCollection<ChargeNotification> collection;
    ObjectMapper objectMapper;

     public InvoiceRepository(MongoClient client, String databaseName, String collectionName, ObjectMapper mapper) {
         val mongoCollection = client.getDatabase(databaseName).getCollection(collectionName);
         JacksonMongoCollection.JacksonMongoCollectionBuilder<ChargeNotification> builder = JacksonMongoCollection.builder();
         this.collection = builder.withObjectMapper(mapper).build(mongoCollection, ChargeNotification.class);
         this.objectMapper = mapper;
     }
}
