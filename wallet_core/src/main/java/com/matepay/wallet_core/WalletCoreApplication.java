package com.matepay.wallet_core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class WalletCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletCoreApplication.class, args);
	}

}
