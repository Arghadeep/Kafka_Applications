package com.sapient.kafka.streaming.creditCardDataProcessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.kafka.streaming.creditCardDataProcessor.entity.Customer;
import com.sapient.kafka.streaming.creditCardDataProcessor.repository.CustomerRepository;
import com.sapient.kafka.streaming.creditCardDataProcessor.service.TransactionMessageProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionMessageProcessorTest {

    private TransactionMessageProcessor transactionMessageProcessor;
    private CustomerRepository customerRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        customerRepository = mock(CustomerRepository.class);
        transactionMessageProcessor = new TransactionMessageProcessor();
        //transactionMessageProcessor.customerRepository = customerRepository;
    }

    @Test
    public void testProcessTransactionMessage_withValidCustomer() throws Exception {
        // Sample transaction JSON
        String transactionMessage = "{ \"transactionID\": \"550e8400-e29b-41d4-a716-446655440000\","
                + "\"txnAmount\": 100.50, \"txnCurrency\": \"USD\", \"transactionType\": \"A\","
                + "\"transactionTimestamp\": \"2024-09-15 12:35:45.123\", \"cardholderPAN\": \"AAAAA1234A\" }";

        // Sample customer fetched from Spanner
        Customer mockCustomer = new Customer("John Doe", "AAAAA1234A", "123 Main St, City, State, ZIP",
                "1985-06-15", "Male", 5000.00);

        when(customerRepository.findById("AAAAA1234A")).thenReturn(Optional.of(mockCustomer));

        // Process the message
        String curatedMessage = transactionMessageProcessor.processTransactionMessage(transactionMessage);

        // Validate the curated message
        JsonNode curatedTransaction = objectMapper.readTree(curatedMessage);
        assertEquals("John Doe", curatedTransaction.get("customerName").asText());
        assertEquals("550e8400-e29b-41d4-a716-446655440000", curatedTransaction.get("transactionID").asText());
        assertEquals(100.50, curatedTransaction.get("txnAmount").asDouble());
    }

    @Test
    public void testProcessTransactionMessage_withMissingCustomer() throws Exception {
        // Sample transaction JSON
        String transactionMessage = "{ \"transactionID\": \"550e8400-e29b-41d4-a716-446655440000\","
                + "\"txnAmount\": 100.50, \"txnCurrency\": \"USD\", \"transactionType\": \"A\","
                + "\"transactionTimestamp\": \"2024-09-15 12:35:45.123\", \"cardholderPAN\": \"AAAAA5678B\" }";

        // Simulate customer not found
        when(customerRepository.findById("AAAAA5678B")).thenReturn(Optional.empty());

        // Process the message
        String curatedMessage = transactionMessageProcessor.processTransactionMessage(transactionMessage);

        // Validate that the message is null due to missing customer
        assertEquals(null, curatedMessage);
    }
}