package com.example.server.repository

import com.example.server.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface UserRepository: MongoRepository<User, String> {

    fun findByEmailAndPassword(email: String, password: String): User?
    fun findByUsernameAndPassword(username: String, password: String): User?
    fun existsByNickname(fieldName: String): Boolean
    fun existsByUsername(fieldName: String): Boolean
    fun existsByEmail(fieldName: String): Boolean
    fun findByNickname(nickname: String): User?
    fun findByUsername(username: String): User?
    fun findUserByEmail(email: String): User?

    @Query("{ 'nickname': { \$regex: ?0, \$options: 'i' } }")
    fun findAllByNicknameLike(nickname: String): List<User>

}