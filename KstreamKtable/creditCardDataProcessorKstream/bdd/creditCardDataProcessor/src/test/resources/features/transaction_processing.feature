Feature: Transaction Processing

  Scenario: Process valid transaction message
    Given a transaction message with transactionID "550e8400-e29b-41d4-a716-446655440000" and PAN "AAAAA1234A"
    And customer with PAN "AAAAA1234A" exists in Spanner
    When the transaction message is processed
    Then the curated message should contain the customer name "John Doe"
    And the transactionID should be "550e8400-e29b-41d4-a716-446655440000"

  Scenario: Process transaction message with missing customer
    Given a transaction message with transactionID "550e8400-e29b-41d4-a716-446655440000" and PAN "AAAAA5678B"
    And customer with PAN "AAAAA5678B" does not exist in Spanner
    When the transaction message is processed
    Then no curated message should be produced