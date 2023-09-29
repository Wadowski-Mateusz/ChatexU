package com.example.server.controller

import com.example.server.model.User
import com.example.server.repository.UserRepository
import com.example.server.service.UserService
import lombok.AllArgsConstructor
import org.bson.types.ObjectId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/user")
@AllArgsConstructor
class UserController(private val userService: UserService) {

    @PostMapping("/add")
    fun addUser(): User = userService.insert(User())

    @GetMapping("/get")
    fun getUser(): ResponseEntity<User> {
        val users: List<User> = userService.findAll()

        return if (users.isNotEmpty())
            ResponseEntity.ok(users.first())
        else
             ResponseEntity.noContent().build()

    }



}