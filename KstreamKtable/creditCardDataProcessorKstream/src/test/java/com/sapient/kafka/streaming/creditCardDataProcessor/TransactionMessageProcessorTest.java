// Unit Test: TransactionMessageProcessorTest
package com.sapient.kafka.streaming.creditCardDataProcessor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sapient.kafka.streaming.creditCardDataProcessor.model.RawTransaction;
import com.sapient.kafka.streaming.creditCardDataProcessor.model.CuratedTransaction;
import com.sapient.kafka.streaming.creditCardDataProcessor.service.TransactionMessageProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionMessageProcessorTest {

	private TransactionMessageProcessor transactionMessageProcessor;

	@BeforeEach
	void setUp() {
		transactionMessageProcessor = new TransactionMessageProcessor();
	}

	@Test
	void processTransaction_validInput_success() {
		RawTransaction rawTransaction = new RawTransaction("a",
				"a@gmail.com", "23456788999", "VISA", "08/29", "700126", "xbnjjjhgg", "abdhhjhj-ghhhethn-hjj", "Timestamp",
				"DEBIT", 14567.09, 1.09, "INR");
		boolean result = transactionMessageProcessor.isValid(rawTransaction);

		assertNotNull(result);
		assertTrue(result);
	}

	@Test
	void processTransaction_nullInput_throwsException() {
		assertThrows(NullPointerException.class, () -> transactionMessageProcessor.isValid(null));
	}
}
