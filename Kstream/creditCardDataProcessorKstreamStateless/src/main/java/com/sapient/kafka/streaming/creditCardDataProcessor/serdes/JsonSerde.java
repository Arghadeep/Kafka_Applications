package com.sapient.kafka.streaming.creditCardDataProcessor.serdes;

import org.apache.kafka.common.serialization.Serdes;

public class JsonSerde<T> extends Serdes.WrapperSerde<T> {
    public JsonSerde(Class<T> type) {
        super(new JsonSerializer<>(), new JsonDeserializer<>(type));
    }
}

