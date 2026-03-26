package com.example.client

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HelloClientTest {
    @Test
    fun greetReturnsHelloWorld() {
        val client = HelloClient()
        assertEquals("Hello world", client.greet())
    }
}
