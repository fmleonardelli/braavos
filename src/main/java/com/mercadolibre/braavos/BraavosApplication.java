package com.mercadolibre.braavos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {MongoDataAutoConfiguration.class, MongoRepositoriesAutoConfiguration.class, KafkaAutoConfiguration.class})
@EnableScheduling
@EnableAsync
public class BraavosApplication {

	public static void main(String[] args) {
		SpringApplication.run(BraavosApplication.class, args);
	}

}
