package com.mercadolibre.braavos.charges.kafka;

import com.mercadolibre.lannister.charges.EventApi;
import org.springframework.stereotype.Component;

@Component
public class KafkaListener {

    @org.springframework.kafka.annotation.KafkaListener(topics = "${message.topic.name}", groupId = "${message.group.name}", containerFactory = "kafkaListenerContainerFactory")
    public void channel(EventApi event) throws Exception {
        System.out.println("Recieved Message of topic1 in  listener: " + event);
    }

}
