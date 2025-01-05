package com.example.server.service

import com.example.server.commons.Constants
import com.example.server.commons.default
import com.example.server.dto.ChatViewDto
import com.example.server.model.*
import com.example.server.repository.ChatRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest
import java.lang.reflect.Method
import java.time.Instant

@SpringBootTest
class ChatServiceTest{


    @Mock
    lateinit var chatRepository: ChatRepository
    @Mock
    lateinit var userService: UserService
    @Mock
    lateinit var messageService: MessageService
    @InjectMocks
    lateinit var chatService: ChatService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        `when`(messageService.findMessageById(chat.lastMessageId))
            .thenReturn(message)
        `when`(userService.getUserById(user2.userId))
            .thenReturn(user2)
    }

    @Test
    fun convertUserToUserChatToChatView() {
        val iconAsByteArray = user2.getIconAsByteArray()

        val correctChatViewDto = ChatViewDto(
            chatId = chat.chatId.toHexString(),
            chatName = user2.nickname,
            lastMessageType = message.messageType,
            lastMessageSender = message.senderId.toHexString(),
            timestamp = message.creationTime,
            icon = iconAsByteArray
        )

        val chatViewDto = chatService.convertUserToUserChatToChatView(chat, user1.userId.toHexString())

        assertEquals(correctChatViewDto, chatViewDto)
    }

    @Test
    fun getLastChatMessage() {

        // when
        val method: Method = ChatService::class.java.getDeclaredMethod("getLastChatMessage", Chat::class.java)
        method.isAccessible = true
        val lastMessage = method.invoke(chatService, chat) as Message

        // then
        assertEquals(lastMessage, message)
    }

    @Test
    fun getLastChatMessageIfNoMessagesSent() {

        // when
        val method: Method = ChatService::class.java.getDeclaredMethod("getLastChatMessage", Chat::class.java)
        method.isAccessible = true
        val lastMessage = method.invoke(chatService, chat.copy(lastMessageId = ObjectId().default())) as Message

        // then
        assertEquals(lastMessage, Message.initMessage(chat))
    }

    companion object {

        lateinit var user1: User
        lateinit var user2: User
        lateinit var message: Message
        lateinit var chat: Chat

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

        }
    }


}