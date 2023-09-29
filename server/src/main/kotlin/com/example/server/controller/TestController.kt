package com.example.server.controller

import com.example.server.model.TestDoc
import com.example.server.repository.TestRepository
import lombok.AllArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
@AllArgsConstructor
class TestController(private val testRepository: TestRepository) {



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