package com.matepay.wallet_core;

import com.matepay.events.EventDispatcher;
import com.matepay.events.EventDispatcherImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {
    @Bean
    public EventDispatcher createTransactionDispatcher() {
        return new EventDispatcherImpl();
    }
}
