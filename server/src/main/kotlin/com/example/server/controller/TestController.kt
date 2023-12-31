package com.example.server.controller

import com.example.server.commons.Constants
import com.example.server.commons.default
import com.example.server.dto.MessageDto
import com.example.server.model.Message
import com.example.server.model.MessageType
import com.example.server.model.TestDoc
import com.example.server.model.User
import com.example.server.repository.TestRepository
import com.example.server.repository.UserRepository
import com.example.server.service.ChatService
import com.example.server.service.MessageService
import lombok.AllArgsConstructor
import org.bson.types.ObjectId
import org.jetbrains.annotations.TestOnly
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import kotlin.math.abs
import kotlin.random.Random

@RestController
@RequestMapping("/test")
@AllArgsConstructor
@CrossOrigin
class TestController(
    private val testRepository: TestRepository,
    private val messageService: MessageService,
    private val userRepository: UserRepository,
    private val chatService: ChatService,
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
            messageType =MessageType.Text("Content for testing purpose"),
            isEdited = false,
            deletedBy = listOf(),
            replyTo = ObjectId().default()
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
            messageType =MessageType.Text("Content for testing purpose"),
            isEdited = false,
            deletedBy = listOf(),
            replyTo = ObjectId().default()
        )
        return messageService.convertMessageToDtoAsSender(m)
    }

    @PostMapping("/add_msg3")
    fun add3(@RequestBody m: MessageDto): MessageDto {
        val m2 = messageService.convertMessageDtoToMessage(m)
        val m3 = messageService.save(m2)
        return messageService.convertMessageToDtoAsSender(m3)
    }


    @PostMapping("/register")
    fun addUserFast(): String {
        val random = abs(Random.nextInt() % 100)
        val user = User(
            userId = ObjectId(),
            nickname = "User$random",
            email = "email${random}@mail.com",
            password = "pass",
            profilePictureUri = Constants.DEFAULT_PROFILE_URI,
            friends = setOf(),
            blockedUsers = setOf()
        )
        userRepository.save(user)
        return user.userId.toHexString()
    }


    @TestOnly
    @PostMapping("/users_chat")
    fun createUsersAndChat(): List<String> {
        val random = { abs(Random.nextInt() % 100) }
        val user = User(
            userId = ObjectId(),
            nickname = "User${random()}",
            email = "email${random()}@mail.com",
            password = "pass",
            profilePictureUri = Constants.DEFAULT_PROFILE_URI,
            friends = setOf(),
            blockedUsers = setOf()
        )
        userRepository.save(user)
        val user2 = User(
            userId = ObjectId(),
            nickname = "User${random()}",
            email = "email${random()}@mail.com",
            password = "pass",
            profilePictureUri = Constants.DEFAULT_PROFILE_URI,
            friends = setOf(),
            blockedUsers = setOf()
        )
        userRepository.save(user)
        userRepository.save(user2)

        val participants = listOf(user.userId.toHexString(), user2.userId.toHexString())

        val chatId: String = chatService.createChat(participants.toSet()).chatId.toHexString()

        return listOf(
            "user1: ${user.userId.toHexString()}",
            "user2: ${user2.userId.toHexString()}",
            "chat: $chatId"
        )

    }


}