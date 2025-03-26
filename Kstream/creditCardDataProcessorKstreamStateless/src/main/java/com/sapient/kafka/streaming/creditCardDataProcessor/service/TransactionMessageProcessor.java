package com.sapient.kafka.streaming.creditCardDataProcessor.service;

import com.sapient.kafka.streaming.creditCardDataProcessor.entity.Customer;
import com.sapient.kafka.streaming.creditCardDataProcessor.model.CuratedTransaction;
import com.sapient.kafka.streaming.creditCardDataProcessor.model.RawTransaction;
import com.sapient.kafka.streaming.creditCardDataProcessor.repository.CustomerRepository;
import lombok.extern.flogger.Flogger;
import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Flogger
public class TransactionMessageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(TransactionMessageProcessor.class);

    @Autowired
    CustomerRepository customerRepository;

    public CuratedTransaction processTransaction(String key, RawTransaction rawTransaction) {
        // Extract the PAN and filter it out (PII/PCI)
        String cardholderPAN = rawTransaction.getCardholderPAN();

        // Fetch the customer details from Spanner using PAN
        Customer customer = customerRepository.findById(cardholderPAN).stream().findFirst().orElse(null);
        CuratedTransaction curatedTransaction = new CuratedTransaction();

        if (customer != null) {
            // Enrich transaction with customer details
            curatedTransaction.setCustomerName(customer.getCustomerName());
            curatedTransaction.setCustomerAddress(customer.getCustomerAddress());
            curatedTransaction.setCustomerDOB(customer.getCustomerDOB());
            curatedTransaction.setCustomerGender(customer.getCustomerGender());
            curatedTransaction.setMonthlyIncome(customer.getMonthlyIncome());
        }

        // Copy relevant fields from raw to curated transaction (filter out PII)
        curatedTransaction.setCardNetwork(rawTransaction.getCardNetwork());
        curatedTransaction.setCardExpiryDate(rawTransaction.getCardExpiryDate());
        curatedTransaction.setBillingPostcode(rawTransaction.getBillingPostcode());
        curatedTransaction.setTransactionID(rawTransaction.getTransactionID());
        curatedTransaction.setTransactionTimestamp(rawTransaction.getTransactionTimestamp());
        curatedTransaction.setTransactionType(rawTransaction.getTransactionType());
        curatedTransaction.setTxnAmount(rawTransaction.getTxnAmount());
        curatedTransaction.setTxnFee(rawTransaction.getTxnFee());
        curatedTransaction.setTxnCurrency(rawTransaction.getTxnCurrency());

        return curatedTransaction;
    }

    public boolean isValid(RawTransaction transaction) {
        // Basic validation logic for transactions
        return transaction.getTransactionID() != null && transaction.getCardholderPAN() != null;
    }

    public String handleInvalidTransaction(RawTransaction transaction) {
        log.atSevere().log("Invalid Transaction with record: %s", transaction.getTransactionID());
        return "Invalid Transaction: " + transaction;
    }
}