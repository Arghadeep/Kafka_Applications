package com.sapient.kafka.streaming.creditCardDataProcessor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.kafka.streaming.creditCardDataProcessor.entity.Customer;
import com.sapient.kafka.streaming.creditCardDataProcessor.joiner.CustomerDetailsJoiner;
import com.sapient.kafka.streaming.creditCardDataProcessor.model.CuratedTransaction;
import com.sapient.kafka.streaming.creditCardDataProcessor.model.RawTransaction;
import com.sapient.kafka.streaming.creditCardDataProcessor.serdes.JsonDeserializer;
import com.sapient.kafka.streaming.creditCardDataProcessor.serdes.JsonSerializer;
import lombok.extern.flogger.Flogger;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.stereotype.Component;

@Component
@Flogger
public class TransactionStreamingService {

    private TransactionMessageProcessor transactionMessageProcessor;

    private  StreamsBuilder streamsBuilder;

    private ObjectMapper objectMapper = new ObjectMapper();

    private CustomerDetailsJoiner customerDetailsJoiner = new CustomerDetailsJoiner();


    private final Serde<RawTransaction> rawTransactionSerde = Serdes.serdeFrom(new
            JsonSerializer<>(), new JsonDeserializer<>(RawTransaction.class));

    private final Serde<CuratedTransaction> curatedTransactionSerde = Serdes.serdeFrom(new
            JsonSerializer<>(), new JsonDeserializer<>(CuratedTransaction.class));

    private final Serde<Customer> customerSerde = Serdes.serdeFrom(new
            JsonSerializer<>(), new JsonDeserializer<>(Customer.class));
    @Autowired
    public TransactionStreamingService(TransactionMessageProcessor transactionMessageProcessor, StreamsBuilder streamsBuilder) {
        this.transactionMessageProcessor = transactionMessageProcessor;
        this.streamsBuilder = streamsBuilder;
    }


    @Autowired
    public void readTransactions() {

        KStream<String, RawTransaction> rawTransactionStream = getKStream();

        rawTransactionStream
                .filter((key, value) -> transactionMessageProcessor.isValid(value)) // Validate transaction
                .join(getKTableForCustomerData(),customerDetailsJoiner)// Filter PII and enrich with customer info
                .peek(((key, value) -> log.atInfo().log("Publishing curated record to credit_card_auth_curated_events with key: %s", key)))
                .to("credit_card_auth_curated_events", Produced.with(Serdes.String(), curatedTransactionSerde)); // Send to target topic

        rawTransactionStream
                .filterNot((key, value) -> transactionMessageProcessor.isValid(value))
                .mapValues(transactionMessageProcessor::handleInvalidTransaction) // Handle invalid transactions
                .peek(((key, value) -> log.atSevere().log("Publishing curated record to credit_card_auth_data_dlq with key: %s", key)))
                .to("credit_card_auth_data_dlq", Produced.with(Serdes.String(), Serdes.String())); // Send to error topic


    }

    private KStream<String, RawTransaction> getKStream() {
        return streamsBuilder.stream("credit_card_auth_raw_events", Consumed.with(Serdes.String(),rawTransactionSerde));
    }

    private KTable<String, Customer> getKTableForCustomerData() {
        return streamsBuilder.table("credit_card_customer_data",
                Consumed.with(Serdes.String(), customerSerde));
    }
}
