package com.example.client;

import com.example.Hello;

public class HelloClient {

  public String greet() {
    Hello hello = new Hello();
    return hello.message();
  }
}
