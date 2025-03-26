package com.sapient.kafka.streaming.creditCardDataProducer.repository;

import com.google.cloud.spring.data.spanner.repository.SpannerRepository;
import com.sapient.kafka.streaming.creditCardDataProducer.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends SpannerRepository<Customer, String> {
}
