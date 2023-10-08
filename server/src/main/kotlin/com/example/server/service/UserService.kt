package com.example.server.service

import com.example.server.exceptions.ErrorMessageCommons
import com.example.server.exceptions.UserNotFoundException
import com.example.server.model.User
import com.example.server.repository.UserRepository
import lombok.AllArgsConstructor
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.util.*

@Service
@AllArgsConstructor
class UserService(private val userRepository: UserRepository) {

    fun save(user: User): User {
        return userRepository.insert(user)
    }

    fun findUserById(userId: String): User {
        return userRepository.findByUserId(ObjectId(userId))
            ?: throw UserNotFoundException(ErrorMessageCommons.idNotFound(type = "user", id = userId))
    }

    fun findUserById(userId: ObjectId): User {
        return userRepository.findByUserId(userId)
            ?: throw UserNotFoundException(ErrorMessageCommons.idNotFound(type = "user", id = userId.toHexString()))
    }




    fun findAll(): List<User> {
        return userRepository.findAll()
    }



}
