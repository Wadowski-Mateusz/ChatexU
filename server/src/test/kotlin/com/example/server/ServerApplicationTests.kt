package com.example.server

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.test.context.ContextConfiguration

    @SpringBootTest
    @ContextConfiguration(classes = [TestConfig::class])
    @Profile("test")
    class ServerApplicationTests {

        @Test
        fun contextLoads() {
        }

    }
