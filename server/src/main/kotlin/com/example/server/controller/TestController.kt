package com.example.server.controller

import com.example.server.commons.Constants
import com.example.server.model.Message
import com.example.server.model.User
import com.example.server.repository.FriendRequestRepository
import com.example.server.repository.UserRepository
import com.example.server.service.ChatService
import com.example.server.service.MessageService
import com.example.server.service.UserService
//import lombok.AllArgsConstructor
import org.bson.types.ObjectId
import org.jetbrains.annotations.TestOnly
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import kotlin.math.abs
import kotlin.random.Random

@RestController
@RequestMapping("/test")
class TestController(
    private val friendRequestRepository: FriendRequestRepository,
    private val messageService: MessageService,
    private val userRepository: UserRepository,
    private val chatService: ChatService,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
) {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello World!" 
    }

//    @PostMapping("/add_msg")
//    fun add2(): Message {
//        val m = Message(
//            messageId =ObjectId(),
//            senderId = ObjectId(),
//            chatId = ObjectId(),
//            timestamp = Instant.now(),
//            messageType = MessageType.Text("Content for testing purpose"),
//            isEdited = false,
//            deletedBy = listOf(),
//            replyTo = ObjectId().default()
//        )
//        return m
//    }


//    @PostMapping("/add_msg2")
//    fun add3(): MessageDto {
//        val m = Message(
//            messageId =ObjectId(),
//            senderId = ObjectId(),
//            chatId = ObjectId(),
//            timestamp = Instant.now(),
//            messageType =MessageType.Text("Content for testing purpose"),
//            isEdited = false,
//            deletedBy = listOf(),
//            replyTo = ObjectId().default()
//        )
//        return messageService.convertMessageToDtoAsSender(m)
//    }

//    @PostMapping("/add_msg3")
//    fun add3(@RequestBody m: MessageDto): MessageDto {
//        val m2 = messageService.convertMessageDtoToMessage(m)
//        val m3 = messageService.save(m2)
//        return messageService.convertMessageToDtoAsSender(m3)
//    }


//    @TestOnly
//    @PostMapping("/register")
//    fun addUserFast(): String {
//        val random = abs(Random.nextInt() % 100)
//        val user = User(
//            userId = ObjectId(),
//            nickname = "User$random",
//            username = "User$random",
//            email = "email${random}@mail.com",
//            password = "pass",
//            profilePictureUri = Constants.DEFAULT_PROFILE_URI,
//            friends = setOf(),
//            blockedUsers = setOf(),
//            tokens = emptyMap(),
//            role = Constants.ROLE_USER
//        )
//        userRepository.save(user)
//        return user.userId.toHexString()
//    }


    @TestOnly
    @PostMapping("/users_chat")
    fun createUsersAndChat(): List<String> {

        println("TestController.createUsersAndChat()")
//        println("Inside create 1")
        val random = { abs(Random.nextInt() % 100) }

        val userId1 = ObjectId()
        val userId2 = ObjectId()
        var rand = random()

        val user = User(
            userId = userId1,
            nickname = "User$rand",
            username = "User$rand",
            email = "email$rand@mail.com",
            password = passwordEncoder.encode("pass"),
            profilePictureUri = Constants.DEFAULT_PROFILE_URI,
            friends = setOf(userId2.toString()),
            role = Constants.ROLE_USER
        )
        rand = random()

        val user2 = User(
            userId = userId2,
            nickname = "User$rand",
            username = "User$rand",
            email = "email$rand@mail.com",
            password = passwordEncoder.encode("pass"),
            profilePictureUri = Constants.DEFAULT_PROFILE_URI,
            friends = setOf(userId1.toString()),
            role = Constants.ROLE_USER
        )
        userRepository.save(user)
        userRepository.save(user2)

        val jwt: String = userService.createToken(user)
        userService.saveToken(user, jwt)
        val jwt2: String = userService.createToken(user2)
        userService.saveToken(user2, jwt2)

        val participants = listOf(user.userId.toHexString(), user2.userId.toHexString())
        val chatId: String = chatService.getOrCreateChat(participants.toSet()).chatId.toHexString()

        return listOf(
            "user1: ${user.userId.toHexString()}",
            "user2: ${user2.userId.toHexString()}",
            "chat: $chatId"
        )

    }

//    @TestOnly
//    @GetMapping("/requests")
//    fun allRequests(): List<FriendRequestDto> {
//        val a = friendRequestRepository.findAll()
//        return a.map { FriendRequestMapper.toDto(it) }
//    }

//    @TestOnly
//    @GetMapping("/getUserByEmail/{userEmail}")
//    fun getUserByEmail(
//        @PathVariable("userEmail")userEmail: String?
//    ): ResponseEntity<UserDto> {
//
//        if (userEmail.isNullOrBlank()) {
//            return ResponseEntity(HttpStatus.BAD_REQUEST)
//        }
//
//        try {
//            val user: User = userService.getUserByEmail(userEmail)
//            val userDto: UserDto = userService.convertToDto(user)
//            return ResponseEntity(userDto, HttpStatus.OK)
//        } catch (e: UserNotFoundException) {
//            return ResponseEntity(HttpStatus.I_AM_A_TEAPOT)
//        } catch (e: Exception) {
//            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
//        }
//    }

    /*
* UNUSED BELOW
* */

//    @TestOnly
//    @GetMapping("/all")
//    fun getAllChats(): ResponseEntity<List<ChatDto>> {
//        return ResponseEntity(
//            chatService.getAll().map{ ChatMapper.toDto(it)},
//            HttpStatus.OK
//        )
//    }
//
//    @TestOnly
//    @GetMapping(value = ["/icon"], produces = [MediaType.IMAGE_JPEG_VALUE])
//    fun getPicture(): ResponseEntity<ByteArray> {
//        return try {
//            val resource: Resource = ClassPathResource("icons/${listOf("red","green","blue").random()}.png")
//            val inp = resource.inputStream.readAllBytes()
//            ResponseEntity.ok(inp)
//        } catch (e: Exception) {
//            ResponseEntity.internalServerError().build()
//        }
//    }
//
//    @TestOnly
//    @GetMapping(
//        value = ["/chat_view/{chatId}"],
////        produces = [MediaType.IMAGE_JPEG_VALUE],
//    ) fun getChatView(
//        @PathVariable("chatId") chatId: String
//    ): ResponseEntity<ChatViewIconDto> {
//        return try {
//            // TODO
//            println("Add URIs")
//            val resource: Resource = ClassPathResource("icons/${listOf("red","green","blue").random()}.png")
//            val inp = resource.inputStream.readAllBytes()
//
//            val chatDto = ChatViewIconDto(
//                chatId,
//                "Test Name",
//                "Test last message",
//                Instant.now(),
//                inp
//            )
//            return ResponseEntity.ok(chatDto)
//        } catch (e: Exception) {
//            ResponseEntity.internalServerError().build()
//        }
//    }
//
//    @TestOnly
//    @GetMapping("/messages/all")
//    fun getAllMessages(): ResponseEntity<List<Message>> {
//        val messages: List<Message> = messageService.getAllMessages()
//        return ResponseEntity.ok(messages)
//    }
//

}