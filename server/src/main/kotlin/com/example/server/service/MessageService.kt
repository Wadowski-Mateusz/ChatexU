package com.example.server.service

import com.example.server.MessageNotFoundException
import com.example.server.model.Message
import com.example.server.repository.MessageRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class MessageService(private val messageRepository: MessageRepository) {

    fun generateRandomMessages(n: Int = 50): List<Message> {
        val messageList: List<Message> = generateSequence {
            Message.Builder().fastBuild()
        }
            .take(n)
            .toList()
        return messageList
    }


    fun getMessage(messageId: UUID): Message {
        return messageRepository.findById(ObjectId(messageId.toString()))
            .orElseThrow { (MessageNotFoundException("Message not found. Id: $messageId")) }
    }

    fun getMessageFromChatInterval(chatId: UUID, from: Instant, to: Instant): List<Message> {
        TODO("MISSING IMPLEMENTATION")
    }

    fun getMessageFromChatBeforeMessage(chatId: UUID, messageId: UUID, messageCount: Int = 30): List<Message> {
        TODO("MISSING IMPLEMENTATION")
    }

    fun postMessage(message: Message): Message {
        TODO("MISSING IMPLEMENTATION")
    }

    fun deleteMessage(messageId: UUID) {
        TODO("MISSING IMPLEMENTATION")
    }

    fun editTextMessage(messageId: UUID, newContent: String) {
        TODO("MISSING IMPLEMENTATION")
    }


}