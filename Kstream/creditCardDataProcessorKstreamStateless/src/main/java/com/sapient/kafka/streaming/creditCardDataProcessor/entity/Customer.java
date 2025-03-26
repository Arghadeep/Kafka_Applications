package com.sapient.kafka.streaming.creditCardDataProcessor.entity;

import com.google.cloud.Date;
import com.google.cloud.spring.data.spanner.core.mapping.Column;
import com.google.cloud.spring.data.spanner.core.mapping.PrimaryKey;
import com.google.cloud.spring.data.spanner.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private Double monthlyIncome;

    // Getters and Setters
}
