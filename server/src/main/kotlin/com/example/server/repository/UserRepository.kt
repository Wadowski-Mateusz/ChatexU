package com.example.server.repository

import com.example.server.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.DeleteQuery
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.Update
import org.springframework.transaction.annotation.Transactional

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


    {'$and':[ {'cif':?0}, {'identification': ?1} , {'mobileNumber' : ?2}, {'applicationStatus' : 'Active' } ] })

    @DeleteQuery(
        "{ _id: ?0 } { \$pull: { friends:  ?1  } }"
    )
    fun removeFriend(userId: String, friendId: String): Any?




}