package com.matepay.events;

import java.time.Instant;

public class EventImpl implements Event {
    private final String name;

    public EventImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Instant getDateTime() {
        return null;
    }

    @Override
    public Payload getPayload() {
        return null;
    }
}
