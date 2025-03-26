package com.sapient.kafka.streaming.creditCardDataProcessor.entity;

import com.google.cloud.Date;
import com.google.cloud.spring.data.spanner.core.mapping.Column;
import com.google.cloud.spring.data.spanner.core.mapping.PrimaryKey;
import com.google.cloud.spring.data.spanner.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {


    private String customerName;

    private String customerPAN;

    private String customerAddress;

    private Date customerDOB;

    private String customerGender;

    private Double monthlyIncome;

    // Getters and Setters
}
