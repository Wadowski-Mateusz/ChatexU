package com.example.server.service

import com.example.server.model.User
import com.example.server.repository.UserRepository
import lombok.AllArgsConstructor
import org.springframework.stereotype.Service

@Service
@AllArgsConstructor
class UserService(private val userRepository: UserRepository) {

    fun insert(user: User): User {
        return userRepository.insert(user)
    }

    fun findAll(): List<User> {

        return userRepository.findAll()
    }


}