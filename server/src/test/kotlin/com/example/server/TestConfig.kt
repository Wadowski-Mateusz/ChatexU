package com.example.server

import com.example.server.service.MessageService
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

@TestConfiguration
@Profile("test")
class TestConfig {
    @Bean
    fun testDataInitializer(messageServiceTest: MessageService): TestDataInitializer {
        return TestDataInitializer(messageServiceTest)
    }
}
