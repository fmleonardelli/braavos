package com.mercadolibre.braavos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mercadolibre.braavos.charges.repository.NotificationRepository;
import com.mongodb.MongoClient;
import io.vavr.jackson.datatype.VavrModule;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MongoDBConfig {

    @Value(value = "${mongo.database}")
    private String databaseName;

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        val objectMapper = new ObjectMapper();
        objectMapper.registerModule(new VavrModule(new VavrModule.Settings().deserializeNullAsEmptyCollection(true)));
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);

        return objectMapper;
    }

    private ObjectMapper mongoObjectMapper() {
        val objectMapper = new ObjectMapper();
        objectMapper.registerModule(new VavrModule(new VavrModule.Settings().deserializeNullAsEmptyCollection(true)));
        objectMapper.registerModule(new CustomDateModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);

        return objectMapper;
    }

    @Bean
    public NotificationRepository notificationRepository(MongoClient mongoClient) {
        return new NotificationRepository(mongoClient, databaseName, "chargeNotifications", mongoObjectMapper());
    }
}
