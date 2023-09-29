package com.example.server.repository

import com.example.server.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepository: MongoRepository<User, UUID> {
    fun findByUserId(restaurantId: String): User?

}