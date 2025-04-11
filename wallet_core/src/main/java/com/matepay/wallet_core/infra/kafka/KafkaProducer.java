package com.matepay.wallet_core.infra.kafka;

import com.matepay.events.Event;
import com.matepay.events.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, Event> kafkaTemplate;

    public void publish(String topic, Event event) {
        kafkaTemplate.send(topic, event);
    }

}
