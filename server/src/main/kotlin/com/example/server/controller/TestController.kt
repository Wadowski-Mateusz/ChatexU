package com.example.server.controller

import com.example.server.commons.default
import com.example.server.dto.MessageDto
import com.example.server.model.Message
import com.example.server.model.MessageType
import com.example.server.model.TestDoc
import com.example.server.repository.TestRepository
import com.example.server.service.MessageService
import lombok.AllArgsConstructor
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

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
    
    @PostMapping("/add_msg")
    fun add2(): Message {
        val m = Message(
            messageId =ObjectId(),
            senderId = ObjectId(),
            chatId = ObjectId(),
            timestamp = Instant.now(),
            messageType =MessageType.Text("Content for testing purpouse"),
            isEdited = false,
            deletedBy = listOf(),
            answerTo = ObjectId().default()
        )
        return m
    }


    @PostMapping("/add_msg2")
    fun add3(): MessageDto {
        val m = Message(
            messageId =ObjectId(),
            senderId = ObjectId(),
            chatId = ObjectId(),
            timestamp = Instant.now(),
            messageType =MessageType.Text("Content for testing purpouse"),
            isEdited = false,
            deletedBy = listOf(),
            answerTo = ObjectId().default()
        )
        return messageService.convertMessageToDtoAsSender(m)
    }

    @PostMapping("/add_msg3")
    fun add3(@RequestBody m: MessageDto): MessageDto {
        val m2 = messageService.convertMessageDtoToMessage(m)
        val m3 = messageService.save(m2)
        return messageService.convertMessageToDtoAsSender(m3)
    }


}