package com.example.server.repository

import com.example.server.model.Message
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface MessageRepository: MongoRepository<Message, String> {
    fun findMessageByMessageId(id: ObjectId): Message?
    fun findMessageByMessageId(id: String): Message?

    fun findByChatId(chatId: ObjectId): List<Message>
    fun findByChatId(chatId: String): List<Message>

    fun findAllMessagesByChatId(chatId: ObjectId): List<Message>

}