package com.example.server.controller

import com.example.server.dto.MessageDto
import com.example.server.dto.toDto
import com.example.server.model.Message
import com.example.server.model.TestDoc
import com.example.server.repository.TestRepository
import com.example.server.service.MessageService
import lombok.AllArgsConstructor
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
@AllArgsConstructor
@CrossOrigin
class TestController(
    private val testRepository: TestRepository,
    private val messageService: MessageService
) {

        @GetMapping("/hello")
        fun hello(): String {
            return "Hello World!"
        }


        @PostMapping("/add")
        fun add(): TestDoc {
            val testDoc = TestDoc().copy(testMsg = "sample")
            return testRepository.insert(testDoc)
        }

}