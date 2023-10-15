package com.example.server.repository

import com.example.server.model.Chat
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ChatRepository: MongoRepository<Chat, String> {

    fun findByParticipantsContains(participant: ObjectId): List<Chat>
//    fun findByParticipantsContains(participant: String): List<Chat>

    fun findByChatId(chatId: ObjectId): Chat?
    fun findByChatId(chatId: String): Chat?

}