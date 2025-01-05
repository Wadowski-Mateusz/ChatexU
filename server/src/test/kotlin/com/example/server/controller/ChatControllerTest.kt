package com.example.server.controller

import com.example.server.commons.Constants
import com.example.server.dto.ChatViewDto
import com.example.server.model.*
import com.example.server.service.ChatService
import com.example.server.service.MessageService
import com.example.server.service.UserService
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.Instant

@SpringBootTest
class ChatControllerTest {


    @Mock
    lateinit var chatService: ChatService
    @Mock
    lateinit var userService: UserService
    @Mock
    lateinit var messageService: MessageService
    @InjectMocks
    lateinit var chatController: ChatController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        
        Mockito.`when`(chatService.findAllChatsByUserId(user1.userId.toHexString()))
            .thenReturn(listOf<Chat>(chat, chat))
        Mockito.`when`(chatService.convertUserToUserChatToChatView(chat, user1.userId.toHexString()))
            .thenReturn(chatViewDto)
        Mockito.`when`(chatService.convertGroupChatToChatView(chat))
            .thenThrow(NotImplementedError::class.java)

        chatController = ChatController(
            chatService = chatService,
            userService = userService,
            messageService = messageService
        )
    }

    @Test
    fun getUserChats() {
        val response: ResponseEntity<List<ChatViewDto>> = chatController.getUserChats(user1.userId.toHexString())

        val correctResponse = ResponseEntity(listOf(chatViewDto, chatViewDto), HttpStatus.OK)

        val body = response.body as List<ChatViewDto>
        val correctBody = correctResponse.body as List<ChatViewDto>

        assertEquals(correctResponse.statusCode, response.statusCode)
        assertEquals(correctBody, body)

    }

    companion object {

        lateinit var user1: User
        lateinit var user2: User
        lateinit var message: Message
        lateinit var chat: Chat
        lateinit var iconAsByteArray: ByteArray
        lateinit var chatViewDto: ChatViewDto
        @JvmStatic
        @BeforeAll
        fun beforeAll() {

            val userID1 = ObjectId()
            val userID2 = ObjectId()
            val messageID1 = ObjectId()
            val chatID1 = ObjectId()

            user1 = User(
                userId = userID1,
                nickname = "Clay Bradley",
                username = "Lula Lindsey",
                email = "dino.reyes@example.com",
                password = "reprimique",
                profilePictureUri = "default.png",
                friends = setOf(userID2.toHexString()),
                token = "",
                role = Constants.ROLE_USER
            )

            user2 = User(
                userId = userID2,
                nickname = "Clay Bradley",
                username = "Lula Lindsey",
                email = "dino.reyes@example.com",
                password = "reprimique",
                profilePictureUri = "default.png",
                friends = setOf(userID1.toHexString()),
                token = "",
                role = Constants.ROLE_USER
            )

            chat = Chat(
                chatId = chatID1,
                lastMessageId = messageID1,
                typeOfChat = ChatType.UserToUser(),
                created = Instant.now(),
                participants = listOf(
                    userID1, userID2
                )
            )

            message = Message(
                messageId = messageID1,
                senderId = userID1,
                chatId = chatID1,
                creationTime = Instant.now(),
                messageType = MessageType.Text("TEST MESSAGE")
            )

            iconAsByteArray = user2.getIconAsByteArray()

            chatViewDto = ChatViewDto(
                chatId = chat.chatId.toHexString(),
                chatName = user2.nickname,
                lastMessageType = message.messageType,
                lastMessageSender = message.senderId.toHexString(),
                timestamp = message.creationTime,
                icon = iconAsByteArray
            )

        }
    }

}