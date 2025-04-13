package com.matepay.wallet_core.messaging;

import java.time.Instant;

public interface Event {
    String getName();
    Instant getDateTime();
    Payload getPayload();
}
