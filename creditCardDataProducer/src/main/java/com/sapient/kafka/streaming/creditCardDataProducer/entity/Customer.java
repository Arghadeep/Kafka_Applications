package com.sapient.kafka.streaming.creditCardDataProducer.entity;

import com.google.cloud.Date;
import com.google.cloud.spanner.Value;
import com.google.cloud.spring.data.spanner.core.mapping.Column;
import com.google.cloud.spring.data.spanner.core.mapping.PrimaryKey;
import com.google.cloud.spring.data.spanner.core.mapping.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Table(name = "customers")
@Getter
@Setter
public class Customer {

    @Column(name = "customerName")
    private String customerName;

    @PrimaryKey
    @Column(name = "customerPan")
    private String customerPAN;

    @Column(name = "customerAddress")
    private String customerAddress;

    @Column(name = "customerDOB")
    private Date customerDOB;

    @Column(name = "customerGender")
    private String customerGender;

    @Column(name = "monthlyIncome")
    private String monthlyIncome;

    // Getters and Setters
}
