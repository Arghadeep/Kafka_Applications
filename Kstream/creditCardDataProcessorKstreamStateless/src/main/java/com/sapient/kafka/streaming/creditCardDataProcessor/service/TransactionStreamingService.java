package com.sapient.kafka.streaming.creditCardDataProcessor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.kafka.streaming.creditCardDataProcessor.entity.Customer;
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
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.Produced;
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


    private final Serde<RawTransaction> rawTransactionSerde = Serdes.serdeFrom(new
            JsonSerializer<>(), new JsonDeserializer<>(RawTransaction.class));

    private final Serde<CuratedTransaction> curatedTransactionSerde = Serdes.serdeFrom(new
            JsonSerializer<>(), new JsonDeserializer<>(CuratedTransaction.class));

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
                .mapValues(transactionMessageProcessor::processTransaction)
                .peek((key, value) ->log.atInfo().log("Publishing Curated Event to credit_card_auth_curated_events with key: %s", key))// Filter PII and enrich with customer info
                .to("credit_card_auth_curated_events", Produced.with(Serdes.String(), curatedTransactionSerde)); // Send to target topic

        rawTransactionStream
                .filterNot((key, value) -> transactionMessageProcessor.isValid(value))
                .mapValues(transactionMessageProcessor::handleInvalidTransaction) // Handle invalid transactions
                .peek((key, value) ->log.atSevere().log("Publishing DLQ Event to credit_card_auth_data_dlq with key: %s", key))
                .to("credit_card_auth_data_dlq", Produced.with(Serdes.String(), Serdes.String())); // Send to error topic


    }

    private KStream<String, RawTransaction> getKStream() {
        return streamsBuilder.stream("credit_card_auth_raw_events", Consumed.with(Serdes.String(),rawTransactionSerde));
    }
}
