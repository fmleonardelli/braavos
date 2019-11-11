package com.mercadolibre.braavos.invoices.kafka;

import com.mercadolibre.braavos.invoices.InvoiceService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaListener {

    public static Logger logger = LoggerFactory.getLogger(KafkaListener.class);

    @Autowired
    InvoiceService invoiceService;

    @org.springframework.kafka.annotation.KafkaListener(topics = "${message.topic.name}", groupId = "${message.group.name}", containerFactory = "kafkaListenerContainerFactory")
    public void listenerEventNotification(EventNotification event) throws Throwable {
        logger.info("listenerEventNotification with params: " + event.toString());
        val res = invoiceService.addCharge(event);
        if (res.isLeft()) {
            logger.error("listenerEventNotification error with params: " + event.toString(), res.getLeft());
            throw res.getLeft();
        }
        logger.info("listenerEventNotification end with params: " + event.toString());
    }
}
