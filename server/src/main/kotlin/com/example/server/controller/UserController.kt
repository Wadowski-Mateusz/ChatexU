package com.example.server.controller

import com.example.server.model.User
import com.example.server.repository.UserRepository
import lombok.AllArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
@AllArgsConstructor
class UserController(private val userRepository: UserRepository) {

    @PostMapping("/add")
    fun addUser(): User = userRepository.insert(User(nickname = "test_add"))

    @GetMapping("/get")
    fun getUser(): ResponseEntity<User> {
        val users: List<User> = userRepository.findAll()

        return if (users.isNotEmpty())
            ResponseEntity.ok(users.first())
        else
             ResponseEntity.noContent().build()

    }



}