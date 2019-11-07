package com.mercadolibre.braavos.invoices.kafka;

import com.mercadolibre.braavos.invoices.repository.InvoiceRepository;
import com.mercadolibre.invoices.model.Invoice;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaListener {

    @Autowired
    InvoiceRepository repository;

    @org.springframework.kafka.annotation.KafkaListener(topics = "${message.topic.name}", groupId = "${message.group.name}", containerFactory = "kafkaListenerContainerFactory")
    public void channel(EventNotification event) throws Exception {
        repository.save(new Invoice(event.getEventId(), event.getAmount(), event.getCurrency(), event.getUserId(), event.getEventType(), event.getDate()));
    }

}
