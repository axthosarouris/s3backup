package com.example.client

import com.example.Hello

class HelloClient {
    fun greet(): String {
        val hello = Hello()
        return hello.message()
    }
}
