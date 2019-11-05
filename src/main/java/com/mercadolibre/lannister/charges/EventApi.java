package com.mercadolibre.lannister.charges;

import lombok.NonNull;

import java.time.Instant;

public class EventApi {

    @NonNull
    Long eventId;

    @NonNull
    Double amount;

    @NonNull
    String currency;

    @NonNull
    Long userId;

    @NonNull
    String eventType;

    @NonNull
    Instant date;

    public EventApi() {
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}
