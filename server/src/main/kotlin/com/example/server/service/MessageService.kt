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

    fun findMessageById(messageId: String): Message {
        return messageRepository.findById(ObjectId(messageId))
            .orElseThrow { (MessageNotFoundException("Message not found. Id: $messageId")) }
    }

    fun getAllMessages(): List<Message> {
        return messageRepository.findAll()
    }

    fun save(message: Message): Message {
        return messageRepository.save(message)
    }

    fun saveAll(messages: List<Message>): List<Message> {
        return messageRepository.saveAll(messages)
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