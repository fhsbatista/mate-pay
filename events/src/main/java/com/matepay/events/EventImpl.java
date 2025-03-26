package com.matepay.events;

import java.time.Instant;

public class EventImpl implements Event {
    @Override
    public String getName() {
        return "";
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
