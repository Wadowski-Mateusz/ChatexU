package com.example.server.controller

import com.example.server.dto.UserDto
import com.example.server.exceptions.UserNotFoundException
import com.example.server.service.UserService
import lombok.AllArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@CrossOrigin
class UserController(private val userService: UserService) {

    @GetMapping("/get/{userId}")
    fun getUser(@PathVariable("userId") userId: String): ResponseEntity<UserDto> {
        return try {
            val user = userService.getById(userId)
            ResponseEntity(userService.convertToDto(user), HttpStatus.OK)
        } catch(e: UserNotFoundException) {
            ResponseEntity(null, HttpStatus.NOT_FOUND)
        } catch(e: Exception) {
            e.printStackTrace()
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("get_all")
    fun getAllUsers(): ResponseEntity<List<UserDto>> {
        val users = userService.findAll()
        val userDtos = users.map { userService.convertToDto(it) }
        return ResponseEntity(userDtos, HttpStatus.OK)
    }

    @PutMapping("/block_user/{blockingUserId}/{blockedUserId}")
    fun blockUser(
        @PathVariable("blockingUserId") blockingUserId: String,
        @PathVariable("blockedUserId") blockedUserId: String
    ): ResponseEntity<Any> {
        return try {
            val blockingUser = userService.getById(blockedUserId)
            userService.getById(blockedUserId) // just to check if user exists
            val updatedBlockedUsersSet = blockingUser.blockedUsers + blockedUserId
            userService.save(blockingUser.copy(blockedUsers = updatedBlockedUsersSet))

            ResponseEntity(HttpStatus.OK)

        } catch (e: UserNotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity( HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/add_friend/{addingUserId}/{addedUserId}")
    fun addFriend(
        @PathVariable("addingUserId") addingUserId: String,
        @PathVariable("addedUserId") addedUserId: String
    ): ResponseEntity<Boolean> {


        // TODO
        // User should send request to another user

        return try {

            val addingUser = userService.getById(addingUserId)
            userService.getById(addedUserId) // just to check if user exists
            val friends = addingUser.friends + addedUserId
            userService.save(addingUser.copy(friends = friends))

            ResponseEntity(HttpStatus.OK)
        } catch (e: UserNotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }


    @DeleteMapping("/delete/{userId}")
    fun deleteUser(@PathVariable("userId") userId: String): ResponseEntity<Boolean> {
        TODO("Not yet implemented")
    }

    @PutMapping("/change/nickname/{userId}/{nickname}")
    fun changeNickname(
        @PathVariable("userId") userId: String,
        @PathVariable("nickname") nickname: String,
    ): ResponseEntity<Boolean> {
        TODO("Not yet implemented")
    }

    @PutMapping("/change/profile_picture/{userId}")
    fun changeProfile(@PathVariable("userId") userId: String) {
        TODO("Not yet implemented")
        // byte array as request body?
    }



}