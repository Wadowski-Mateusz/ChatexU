package com.example.server.repository

import com.example.server.model.Message
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface MessageRepository: MongoRepository<Message, ObjectId> {

}