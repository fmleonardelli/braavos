package com.mercadolibre.braavos.charges.kafka;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Event {

    @NonNull
    Long eventId;

    @NonNull
    Double amount;

    @NonNull
    String currency;

    @NonNull
    String userId;

    @NonNull
    String eventType;

    @NonNull
    Instant date;
}
