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

        @PostMapping("/msg1")
        fun msg1(): List<MessageDto> {
            val l = listOf<Message>(
                Message.Builder().fastBuild(),
                Message.Builder().fastBuild(),
                Message.Builder().fastBuild(),
                Message.Builder().fastBuild(),
                Message.Builder().fastBuild(),
            )
            messageService.saveAll(messages = l)
            val listMessages = messageService.getAllMessages()
            val listDto: List<MessageDto> = listMessages
                .map{
                    it.toDto()
                }
                .toList()
            return listDto
        }



    @PostMapping("/msg2")
    fun msg2(): MessageDto {
        val msg = Message.Builder().fastBuild()
        return messageService.save(msg).toDto()
    }




}