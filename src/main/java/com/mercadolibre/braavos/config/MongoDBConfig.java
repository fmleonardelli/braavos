package com.mercadolibre.braavos.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.mercadolibre.braavos.invoices.repo.InvoiceRepository;
import com.mongodb.MongoClient;
import io.vavr.jackson.datatype.VavrModule;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDBConfig {

    @Value(value = "${mongo.database}")
    private String databaseName;

    private ObjectMapper mongoObjectMapper() {
        val objectMapper = new ObjectMapper();
        objectMapper.registerModule(new VavrModule(new VavrModule.Settings().deserializeNullAsEmptyCollection(true)));
        objectMapper.registerModule(new CustomDateModule());
        objectMapper.registerModule(new BigDecimalModule());
        objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }

    @Bean
    public InvoiceRepository invoiceRepository(MongoClient mongoClient) {
        return new InvoiceRepository(mongoClient, databaseName, "invoices", mongoObjectMapper());
    }
}
