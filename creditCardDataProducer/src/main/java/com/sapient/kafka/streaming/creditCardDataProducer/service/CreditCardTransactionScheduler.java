package com.sapient.kafka.streaming.creditCardDataProducer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.kafka.streaming.creditCardDataProducer.entity.Customer;
import com.sapient.kafka.streaming.creditCardDataProducer.model.CreditCardTransaction;
import com.sapient.kafka.streaming.creditCardDataProducer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;

@Component
public class CreditCardTransactionScheduler {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    private static final String CREDIT_CARD_AUTH_RAW_DATA_TOPIC = "credit_card_auth_raw_events";

    private static final String CUSTOMER_DATA_TOPIC = "credit_card_customer_data";

    @Scheduled(fixedRate = 1)  // Every 1 ms
    public void generateAndPublishTransaction() {
        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        if (customers.isEmpty()) {
            System.out.println("No customers found in the Spanner database.");
            return;
        }

        // Randomly select a customer
        Customer randomCustomer = customers.get(random.nextInt(customers.size()));

        // Generate credit card transaction
        CreditCardTransaction transaction = new CreditCardTransaction(
                randomCustomer.getCustomerName(), randomCustomer.getCustomerPAN()
        );

        try {
            // Convert the transaction to JSON
            String transactionJson = objectMapper.writeValueAsString(transaction);
            // Publish to Kafka
            kafkaProducerService.sendMessage(randomCustomer.getCustomerPAN(), transactionJson, CREDIT_CARD_AUTH_RAW_DATA_TOPIC);
            System.out.println("Published Transaction: " + transactionJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void generateAndPublishCustomerDetails() {
        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        if (customers.isEmpty()) {
            System.out.println("No customers found in the Spanner database.");
            return;
        }


            // Publish to Kafka
            customers.forEach(customer -> {
                try {
                    kafkaProducerService.sendMessage(customer.getCustomerPAN(), objectMapper.writeValueAsString(customer), CUSTOMER_DATA_TOPIC);
                    System.out.println("Published Customer Details with: "+ objectMapper.writeValueAsString(customer));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
    }
}
