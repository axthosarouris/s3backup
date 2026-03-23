Feature: Hello message

  Scenario: Get hello world message
    Given a Hello instance
    When I call the message method
    Then it should return "Hello world"