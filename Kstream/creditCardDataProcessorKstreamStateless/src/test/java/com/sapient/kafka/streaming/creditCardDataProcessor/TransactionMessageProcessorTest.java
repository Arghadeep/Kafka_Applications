//package com.sapient.kafka.streaming.creditCardDataProcessor;
//
//import com.sapient.kafka.streaming.creditCardDataProcessor.entity.Customer;
//import com.sapient.kafka.streaming.creditCardDataProcessor.model.CuratedTransaction;
//import com.sapient.kafka.streaming.creditCardDataProcessor.model.RawTransaction;
//import com.sapient.kafka.streaming.creditCardDataProcessor.service.TransactionMessageProcessor;
//import org.apache.kafka.streams.state.KeyValueStore;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class TransactionMessageProcessorTest {
//
//    private KeyValueStore<String, Customer> customerStore;
//    private TransactionMessageProcessor processor;
//
//    @BeforeEach
//    public void setUp() {
//        customerStore = mock(KeyValueStore.class);
//        processor = new TransactionMessageProcessor(customerStore);
//    }
//
//    @Test
//    public void testValidTransactionProcessing() {
//        RawTransaction rawTransaction = new RawTransaction();
//        rawTransaction.setTransactionID("550e8400");
//        rawTransaction.setCardholderPAN("AAAAA1234A");
//
//        Customer customer = new Customer("John Doe", "AAAAA1234A", "123 Main St", "28-09-1996", "MALE", 65000.0);
//        when(customerStore.get("AAAAA1234A")).thenReturn(customer);
//
//        CuratedTransaction curatedTransaction = processor.processTransaction("abc",rawTransaction);
//
//        // Validate the enrichment
//        assertEquals("John Doe", curatedTransaction.getCustomerName());
//        assertEquals("123 Main St", curatedTransaction.getCustomerAddress());
//    }
//
//    @Test
//    public void testInvalidTransactionHandling() {
//        RawTransaction rawTransaction = new RawTransaction();
//        String errorMessage = processor.handleInvalidTransaction(rawTransaction);
//        assertEquals("Invalid Transaction: " + rawTransaction, errorMessage);
//    }
//}