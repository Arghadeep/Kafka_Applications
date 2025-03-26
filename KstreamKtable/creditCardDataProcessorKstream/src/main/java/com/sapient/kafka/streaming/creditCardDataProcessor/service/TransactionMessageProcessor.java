package com.sapient.kafka.streaming.creditCardDataProcessor.service;

import com.sapient.kafka.streaming.creditCardDataProcessor.model.RawTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransactionMessageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(TransactionMessageProcessor.class);

    public boolean isValid(RawTransaction transaction) {
        // Basic validation logic for transactions
        return transaction.getTransactionID() != null && transaction.getCardholderPAN() != null;
    }

    public String handleInvalidTransaction(RawTransaction transaction) {
        return "Invalid Transaction: " + transaction;
    }
}