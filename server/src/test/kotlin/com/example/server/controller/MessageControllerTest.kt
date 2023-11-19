package com.example.server.controller

import com.example.server.commons.default
import com.example.server.model.Message
import com.example.server.model.MessageType
import com.example.server.service.MessageService
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import java.time.Instant


@SpringBootTest
class MessageControllerTest{

//    @InjectMocks
//    lateinit var messageRepository: MessageRepository
//    @Mock
//    lateinit var messageService: MessageService
//    @Inject
//    lateinit var messageController: MessageController

    private final val messageService: MessageService= mock<MessageService>()
    val messageController: MessageController = MessageController(messageService)

    private val messages: List<Message> by lazy {
        val msg = {
            Message(
                messageId = ObjectId(),
                senderId = ObjectId(),
                chatId = ObjectId(),
                timestamp = Instant.now(),
                messageType = MessageType.Text("test message"),
                isEdited = false,
                deletedBy = listOf(),
                replyTo = ObjectId().default()
            )
        }
        generateSequence(msg)
            .take(5)
            .toList()
    }

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun getAll() {
        Mockito.`when`(messageService.getAllMessages()).thenReturn(messages)

        val response = messageController.getAll()

        assert(response.statusCode == HttpStatus.OK) {"Bad HTTP status code. Expected: ${HttpStatus.OK}; Actual: ${response.statusCode}"}

        val messagesFromResponse = response.body!!
        assert(messagesFromResponse.isNotEmpty()) {"List shouldn't be empty"}
        assert(messagesFromResponse.containsAll(messages)) { "Messages are not the same" }
    }

//    @Test
//    fun findMessageById() {
//    }
//    @Test
//    fun sendMessage() {
//    }
//
//    @Test
//    fun updateMessage() {
//    }
//
//    @Test
//    fun deleteMessage() {
//    }


}