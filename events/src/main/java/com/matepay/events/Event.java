package com.matepay.events;

import java.time.Instant;

public interface Event {
    String getName();
    Instant getDateTime();
    Payload getPayload();
}
