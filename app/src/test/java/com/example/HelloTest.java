package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HelloTest {

  @Test
  void messageReturnsHelloWorld() {
    Hello hello = new Hello();
    assertEquals("Hello world", hello.message());
  }
}
