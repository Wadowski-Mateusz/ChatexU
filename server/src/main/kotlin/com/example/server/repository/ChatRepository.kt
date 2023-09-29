package com.example.server.repository

import com.example.server.model.Chat
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ChatRepository: MongoRepository<Chat, ObjectId> {

}