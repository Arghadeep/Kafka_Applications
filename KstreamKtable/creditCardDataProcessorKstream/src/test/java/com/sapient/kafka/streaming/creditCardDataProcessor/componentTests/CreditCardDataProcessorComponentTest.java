//// Component Test: TransactionStreamingServiceTest
//package com.sapient.kafka.streaming.creditCardDataProcessor.componentTests;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.sapient.kafka.streaming.creditCardDataProcessor.model.RawTransaction;
//import com.sapient.kafka.streaming.creditCardDataProcessor.service.TransactionStreamingService;
//import org.apache.kafka.streams.KeyValue;
//import org.apache.kafka.streams.TestInputTopic;
//import org.apache.kafka.streams.TestOutputTopic;
//import org.apache.kafka.streams.TopologyTestDriver;
//import org.apache.kafka.common.serialization.Serdes;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//public class CreditCardDataProcessorComponentTest {
//
//	private TopologyTestDriver testDriver;
//	private TestInputTopic<String, RawTransaction> inputTopic;
//	private TestOutputTopic<String, String> outputTopic;
//	private TransactionStreamingService streamingService;
//
//	@BeforeEach
//	void setUp() {
//		streamingService = new TransactionStreamingService();
//		testDriver = new TopologyTestDriver(streamingService.createTopology(), streamingService.getProperties());
//		inputTopic = testDriver.createInputTopic("raw-transactions", Serdes.String().serializer(), Serdes.String().serializer());
//		outputTopic = testDriver.createOutputTopic("curated-transactions", Serdes.String().deserializer(), Serdes.String().deserializer());
//	}
//
//	@AfterEach
//	void tearDown() {
//		testDriver.close();
//	}
//
//	@Test
//	void processValidTransaction_success() {
//		RawTransaction rawTransaction = new RawTransaction("123", "456", 100.0);
//		inputTopic.pipeInput("key1", rawTransaction);
//
//		KeyValue<String, String> result = outputTopic.readKeyValue();
//		assertNotNull(result);
//		assertEquals("key1", result.key);
//		assertTrue(result.value.contains("123"));
//	}
//}
