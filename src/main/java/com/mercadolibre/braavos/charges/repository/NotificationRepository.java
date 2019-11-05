package com.mercadolibre.braavos.charges.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.braavos.charges.kafka.ChargeNotification;
import com.mongodb.MongoClient;
import com.mongodb.client.model.IndexOptions;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.bson.Document;
import org.mongojack.JacksonMongoCollection;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NotificationRepository {

    JacksonMongoCollection<ChargeNotification> collection;
    ObjectMapper objectMapper;

     public NotificationRepository(MongoClient client, String databaseName, String collectionName, ObjectMapper mapper) {
         val mongoCollection = client.getDatabase(databaseName).getCollection(collectionName);
         JacksonMongoCollection.JacksonMongoCollectionBuilder<ChargeNotification> builder = JacksonMongoCollection.builder();
         this.collection = builder.withObjectMapper(mapper).build(mongoCollection, ChargeNotification.class);
         this.objectMapper = mapper;

         this.collection.createIndex(new Document("type", 1));
         this.collection.createIndex(new Document("eventId", 1), new IndexOptions().unique(true));
         this.collection.createIndex(new Document("state", 1));
         this.collection.createIndex(new Document("date", 1));
         this.collection.createIndex(new Document("processedDate", 1));
     }
}
