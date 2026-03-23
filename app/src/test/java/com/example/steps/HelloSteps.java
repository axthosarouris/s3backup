package com.example.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.Hello;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HelloSteps {

  private Hello hello;
  private String result;

  @Given("a Hello instance")
  public void aHelloInstance() {
    hello = new Hello();
  }

  @When("I call the message method")
  public void iCallTheMessageMethod() {
    result = hello.message();
  }

  @Then("it should return {string}")
  public void itShouldReturn(String expected) {
    assertEquals(expected, result);
  }
}
