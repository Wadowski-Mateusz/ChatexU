package com.example.server

import com.example.server.model.Message
import com.example.server.model.MessageType
import com.example.server.service.MessageService
import org.bson.types.ObjectId
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Profile
import java.time.Instant

@TestConfiguration
@Profile("test")
class TestDataInitializer(
    private val messageServiceTest: MessageService
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        val msg = Message(
            messageId = ObjectId(),
            senderId = ObjectId(),
            chatId = ObjectId(),
            creationTime = Instant.now(),
            messageType = MessageType.Text("Test message"),
//            isEdited = false,
//            deletedBy = listOf(),
//            replyTo = ObjectId().default()
        )

        messageServiceTest.save(msg)
    }

}