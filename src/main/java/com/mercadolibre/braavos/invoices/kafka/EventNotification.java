package com.mercadolibre.braavos.invoices.kafka;

import lombok.*;

import java.time.Instant;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EventNotification {
    String userId;
    Instant date;
    String eventType;
    String eventId;
    String currency;
    Double amount;
}
