package com.example.server.repository

import com.example.server.model.Chat
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.Update

interface ChatRepository: MongoRepository<Chat, String> {

    fun findByParticipantsContains(participant: ObjectId): List<Chat>
//    fun findByParticipantsContains(participant: String): List<Chat>

//    fun findOneParticipantsContains(participants: List<ObjectId>): Chat?
    @Query("{'participants' : { \$all : ?0 }}")
    fun findByParticipants(participants: List<ObjectId>): Chat?



    fun findByChatId(chatId: ObjectId): Chat?
    fun findByChatId(chatId: String): Chat?

    @Query("{ '_id' : ?0 }")
    @Update("{ '\$set' : { 'lastMessage' : ?1 } }")
    fun updateLastMessage(chatId: String?, lastMessage: String?): Long

}