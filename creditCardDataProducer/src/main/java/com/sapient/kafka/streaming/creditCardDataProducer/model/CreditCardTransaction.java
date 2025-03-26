package com.sapient.kafka.streaming.creditCardDataProducer.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreditCardTransaction {

    public String cardholderName;
    public String cardholderEmail;
    public String cardNumber;
    public String cardNetwork;
    public String cardExpiryDate;
    public String billingPostcode;
    public String cardholderPAN;
    public String transactionID;
    public String transactionTimestamp;
    public String transactionType;
    public Double txnAmount;
    public Double txnFee;
    public String txnCurrency;

    public CreditCardTransaction(String cardholderName, String cardholderPAN) {
        this.cardholderName = cardholderName;
        this.cardholderEmail = cardholderName.replaceAll(" ", ".").toLowerCase() + "@example.com"; // Generate email from name
        this.cardNumber = generateRandomCardNumber();
        this.cardNetwork = "Visa";  // You can randomize this if needed
        this.cardExpiryDate = "08/2029";
        this.billingPostcode = "12345";
        this.cardholderPAN = cardholderPAN;
        this.transactionID = UUID.randomUUID().toString();
        this.transactionTimestamp = LocalDateTime.now().toString();
        this.transactionType = "A";
        this.txnAmount = generateRandomTxnAmount();
        this.txnFee = 2.50;
        this.txnCurrency = "USD";
    }

    private String generateRandomCardNumber() {
        return "4" + (long) (Math.random() * 100000000000000L);
    }

    private Double generateRandomTxnAmount() {
        return Math.round((10 + Math.random() * 490) * 100.0) / 100.0; // Amount between 10 and 500
    }

    // Getters and Setters
}
