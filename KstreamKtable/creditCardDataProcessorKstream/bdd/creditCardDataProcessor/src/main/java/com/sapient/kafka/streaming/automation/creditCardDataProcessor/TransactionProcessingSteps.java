import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionProcessingSteps {

    @Autowired
    private TransactionMessageProcessor transactionMessageProcessor;

    @Autowired
    private CustomerRepository customerRepository;

    private String transactionMessage;
    private String curatedMessage;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Given("a transaction message with transactionID {string} and PAN {string}")
    public void a_transaction_message_with_transactionID_and_PAN(String transactionID, String pan) {
        transactionMessage = "{ \"transactionID\": \"" + transactionID + "\","
                + "\"txnAmount\": 100.50, \"txnCurrency\": \"USD\", \"transactionType\": \"A\","
                + "\"transactionTimestamp\": \"2024-09-15 12:35:45.123\", \"cardholderPAN\": \"" + pan + "\" }";
    }

    @Given("customer with PAN {string} exists in Spanner")
    public void customer_with_PAN_exists_in_Spanner(String pan) {
        Customer mockCustomer = new Customer("John Doe", pan, "123 Main St, City, State, ZIP",
                "1985-06-15", "Male", 5000.00);
        when(customerRepository.findById(pan)).thenReturn(Optional.of(mockCustomer));
    }

    @Given("customer with PAN {string} does not exist in Spanner")
    public void customer_with_PAN_does_not_exist_in_Spanner(String pan) {
        when(customerRepository.findById(pan)).thenReturn(Optional.empty());
    }

    @When("the transaction message is processed")
    public void the_transaction_message_is_processed() {
        curatedMessage = transactionMessageProcessor.processTransactionMessage(transactionMessage);
    }

    @Then("the curated message should contain the customer name {string}")
    public void the_curated_message_should_contain_the_customer_name(String customerName) throws Exception {
        JsonNode curatedTransaction = objectMapper.readTree(curatedMessage);
        assertEquals(customerName, curatedTransaction.get("customerName").asText());
    }

    @Then("the transactionID should be {string}")
    public void the_transactionID_should_be(String transactionID) throws Exception {
        JsonNode curatedTransaction = objectMapper.readTree(curatedMessage);
        assertEquals(transactionID, curatedTransaction.get("transactionID").asText());
    }

    @Then("no curated message should be produced")
    public void no_curated_message_should_be_produced() {
        assertNull(curatedMessage);
    }
}