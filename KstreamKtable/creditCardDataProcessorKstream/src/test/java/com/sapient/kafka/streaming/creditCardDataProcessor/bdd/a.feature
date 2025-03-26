Feature: Credit Card Transaction Processing
		Scenario: Process valid transaction
		Given a raw transaction with ID "123", customer ID "456", and amount 100.0
		When the transaction is processed
		Then a curated transaction with ID "123" and amount 100.0 is created

		Scenario: Discard invalid transaction
		Given a raw transaction with missing transaction ID
		When the transaction is processed
		Then an error is logged