package com.sapient.kafka.streaming.creditCardDataProcessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
public class CreditCardDataProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditCardDataProcessorApplication.class, args);
	}

}
