package com.matepay.balances.infra.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.matepay.balances.messaging.events.BalanceUpdated;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "balances", groupId = "balances-group")
    public void consume(BalanceUpdated event) {
        System.out.println("Received BalanceUpdated event:");
        System.out.println("From Account: " + event.getPayload().accountIdFrom());
        System.out.println("To Account: " + event.getPayload().accountIdTo());
        System.out.println("Balance From: " + event.getPayload().balanceAccountFrom());
        System.out.println("Balance To: " + event.getPayload().balanceAccountTo());

    }
}
