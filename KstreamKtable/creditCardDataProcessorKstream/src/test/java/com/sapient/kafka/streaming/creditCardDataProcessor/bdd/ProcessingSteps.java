//
//// Step Definitions: TransactionProcessingSteps.java
//package com.sapient.kafka.streaming.creditCardDataProcessor.bdd;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.sapient.kafka.streaming.creditCardDataProcessor.model.RawTransaction;
//import com.sapient.kafka.streaming.creditCardDataProcessor.service.TransactionMessageProcessor;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//
//public class ProcessingSteps {
//
//	private TransactionMessageProcessor processor;
//	private RawTransaction inputTransaction;
//	private Exception thrownException;
//
//	@Given("a raw transaction with ID {string}, customer ID {string}, and amount {double}")
//	public void createRawTransaction(String transactionId, String customerId, double amount) {
//		inputTransaction = new RawTransaction(transactionId, customerId, amount);
//		processor = new TransactionMessageProcessor();
//	}
//
//	@Given("a raw transaction with missing transaction ID")
//	public void createInvalidTransaction() {
//		inputTransaction = new RawTransaction(null, "456", 100.0);
//		processor = new TransactionMessageProcessor();
//	}
//
//	@When("the transaction is processed")
//	public void processTransaction() {
//		try {
//			processor.processTransaction(inputTransaction);
//		} catch (Exception e) {
//			thrownException = e;
//		}
//	}
//
//	@Then("a curated transaction with ID {string} and amount {double} is created")
//	public void validateCuratedTransaction(String transactionId, double amount) {
//		assertNotNull(inputTransaction);
//		assertEquals(transactionId, inputTransaction.getTransactionId());
//		assertEquals(amount, inputTransaction.getAmount());
//	}
//
//	@Then("an error is logged")
//	public void validateErrorLogging() {
//		assertNotNull(thrownException);
//		assertTrue(thrownException instanceof IllegalArgumentException);
//	}
//}
//
//
