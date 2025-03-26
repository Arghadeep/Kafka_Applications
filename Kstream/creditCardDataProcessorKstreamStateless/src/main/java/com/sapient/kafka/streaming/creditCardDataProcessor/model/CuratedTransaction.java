package com.sapient.kafka.streaming.creditCardDataProcessor.model;

import com.google.cloud.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CuratedTransaction {

    private String cardholderName;
    private String cardholderEmail;
    private String cardNetwork;
    private String cardExpiryDate;
    private String billingPostcode;
    private String transactionID;
    private String transactionTimestamp;
    private String transactionType;
    private Double txnAmount;
    private Double txnFee;
    private String txnCurrency;
    private String customerName;
    private String customerAddress;
    private Date customerDOB;
    private String customerGender;
    private Double monthlyIncome;
}
