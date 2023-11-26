package com.example.server.repository

import com.example.server.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String> {

    fun findByEmailAndPassword(email: String, password: String): User?
    fun existsByNickname(fieldName: String): Boolean
    fun existsByEmail(fieldName: String): Boolean
    fun findByNickname(nickname: String): User?

}