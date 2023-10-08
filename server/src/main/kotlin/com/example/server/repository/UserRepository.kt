package com.example.server.repository

import com.example.server.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepository: MongoRepository<User, ObjectId> {
    fun findByUserId(userId: ObjectId): User?

}