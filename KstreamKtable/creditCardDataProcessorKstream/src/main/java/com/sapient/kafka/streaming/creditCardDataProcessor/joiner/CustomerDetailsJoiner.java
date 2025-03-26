package com.sapient.kafka.streaming.creditCardDataProcessor.joiner;

import com.sapient.kafka.streaming.creditCardDataProcessor.entity.Customer;
import com.sapient.kafka.streaming.creditCardDataProcessor.model.CuratedTransaction;
import com.sapient.kafka.streaming.creditCardDataProcessor.model.RawTransaction;
import org.apache.kafka.streams.kstream.ValueJoinerWithKey;

public class CustomerDetailsJoiner implements ValueJoinerWithKey<String, RawTransaction, Customer, CuratedTransaction> {
    @Override
    public CuratedTransaction apply(String readOnlyKey, RawTransaction rawTransaction, Customer customer) {
        return new CuratedTransaction(rawTransaction.getCardholderName(),
                rawTransaction.getCardholderEmail(),
                rawTransaction.getCardNetwork(),
                rawTransaction.getCardExpiryDate(),
                rawTransaction.getBillingPostcode(),
                rawTransaction.getTransactionID(),
                rawTransaction.getTransactionTimestamp(),
                rawTransaction.getTransactionType(),
                rawTransaction.getTxnAmount(),
                rawTransaction.getTxnFee(),
                rawTransaction.getTxnCurrency(),
                customer.getCustomerName(),
                customer.getCustomerAddress(),
                customer.getCustomerDOB(),
                customer.getCustomerGender(),
                customer.getMonthlyIncome());
    }
}
