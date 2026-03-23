package com.example.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HelloClientTest {

  @Test
  void greetReturnsHelloWorld() {
    HelloClient client = new HelloClient();
    assertEquals("Hello world", client.greet());
  }
}
