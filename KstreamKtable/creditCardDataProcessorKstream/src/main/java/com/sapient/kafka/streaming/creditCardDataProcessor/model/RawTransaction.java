package com.sapient.kafka.streaming.creditCardDataProcessor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawTransaction {

    private String cardholderName;
    private String cardholderEmail;
    private String cardNumber;
    private String cardNetwork;
    private String cardExpiryDate;
    private String billingPostcode;
    private String cardholderPAN;
    private String transactionID;
    private String transactionTimestamp;
    private String transactionType;
    private Double txnAmount;
    private Double txnFee;
    private String txnCurrency;

    public RawTransaction(String cardholderName, String cardholderPAN) {
        this.cardholderName = cardholderName;
        this.cardholderEmail = cardholderName.replaceAll(" ", ".").toLowerCase() + "@example.com"; // Generate email from name
        this.cardNumber = generateRandomCardNumber();
        this.cardNetwork = "Visa";  // You can randomize this if needed
        this.cardExpiryDate = "08-25";
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
