package com.sapient.kafka.streaming.creditCardDataProducer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String key, String message, String topicName) {
        kafkaTemplate.send(topicName, key, message);
    }
}
