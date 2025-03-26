package com.sapient.kafka.streaming.creditCardDataProcessor.config;

import com.sapient.kafka.streaming.creditCardDataProcessor.model.CuratedTransaction;
import com.sapient.kafka.streaming.creditCardDataProcessor.model.RawTransaction;
import com.sapient.kafka.streaming.creditCardDataProcessor.serdes.JsonDeserializer;
import com.sapient.kafka.streaming.creditCardDataProcessor.serdes.JsonSerde;
import com.sapient.kafka.streaming.creditCardDataProcessor.serdes.JsonSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafkaStreams
@EnableKafka
public class KafkaConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${streamThreadCount}")
    private String numStreamThreads;

    @Value(value = "${saslUsername}")
    private String saslUsername;

    @Value(value = "${saslPassword}")
    private String saslPassword;

    private static  final String JAAS_CONFIG_FORMAT = "%s required username=\"%s\" password=\"%s\";";

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        Map<String, Object> config = new HashMap<>();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "credit-card-streaming");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapAddress);
        config.put(StreamsConfig.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        config.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        config.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(JAAS_CONFIG_FORMAT, PlainLoginModule.class.getName(), saslUsername, saslPassword));
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);
        config.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, numStreamThreads);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new KafkaStreamsConfiguration(config);
    }
}
