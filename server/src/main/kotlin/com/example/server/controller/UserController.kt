package com.example.server.controller

import com.example.server.converters.FriendRequestMapper
import com.example.server.converters.UserMapper
import com.example.server.dto.FriendRequestDto
import com.example.server.dto.UserDto
import com.example.server.dto.UserViewDto
import com.example.server.exceptions.AlreadyFriendsException
import com.example.server.exceptions.FriendRequestAlreadyExistsException
import com.example.server.exceptions.UserBlockedByGivenUserException
import com.example.server.exceptions.UserNotFoundException
import com.example.server.model.User
import com.example.server.service.FriendRequestService
import com.example.server.service.UserService
import lombok.AllArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@CrossOrigin
class UserController(
    private val userService: UserService,
    private val friendRequestService: FriendRequestService,
) {

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

    @GetMapping("/get_all")
    fun getAllUsers(): ResponseEntity<List<UserDto>> {
        val users = userService.findAll()
        val userDtos = users.map { userService.convertToDto(it) }
        return ResponseEntity(userDtos, HttpStatus.OK)
    }

    @GetMapping("/nickname/{searcherId}/{nickname}")
    fun getUserByNickname(
        @PathVariable("searcherId") searcherId: String,
        @PathVariable("nickname") nickname: String
    ): ResponseEntity<UserViewDto> {
        return try {
            val user: User = userService.getUserByNickname(nickname)
            if(userService.isUserBlockedByGivenUser(user.userId.toString(), searcherId))
                throw UserBlockedByGivenUserException()
            val userViewDto: UserViewDto = UserMapper.toViewDto(user)
            ResponseEntity(userViewDto, HttpStatus.OK)
        } catch (e: UserNotFoundException) {
            ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: UserBlockedByGivenUserException) {
            ResponseEntity(HttpStatus.CONFLICT)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/areFriends/{userId1}/{userId2}")
    fun areFriends(
        @PathVariable("userId1") userId1: String,
        @PathVariable("userId2") userId2: String
    ): ResponseEntity<Boolean> {
        return try {
            ResponseEntity(
                userService.checkIfFriends(userId1, userId2),
                HttpStatus.OK
            )
        } catch (e: UserNotFoundException) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/friendRequest/{userId1}/{userId2}")
    fun doesFriendRequestExist(
        @PathVariable("userId1") userId1: String,
        @PathVariable("userId2") userId2: String
    ): ResponseEntity<Boolean> {
        return try {
            ResponseEntity(
                friendRequestService.doesRequestExistForUsers(userId1, userId2),
                HttpStatus.OK
            )
        } catch (e: UserNotFoundException) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/friendRequest/{userId}")
    fun getAllFriendRequestsForUser(
        @PathVariable("userId") userId: String
    ): ResponseEntity<List<FriendRequestDto>> {
        return try {
            val requests = friendRequestService.findAllRequestsForUserAsRecipient(userId)
                .map { FriendRequestMapper.toDto(it) }
            ResponseEntity(requests, HttpStatus.OK)
        } catch (e: UserNotFoundException) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }



    @PostMapping("/friend/send_request/{senderId}/{recipientId}")
    fun sendFriendRequest(
        @PathVariable("senderId") senderId: String,
        @PathVariable("recipientId") recipientId: String,
    ): ResponseEntity<Any> {
        return try {
            if(userService.checkIfFriends(senderId, recipientId))
                throw AlreadyFriendsException()
            val isRequestCreated: Boolean = userService.createFriendRequest(senderId, recipientId)
            if (isRequestCreated)
                // request created
                ResponseEntity(HttpStatus.CREATED)
            else
                // request not created,
                // because recipient has already send request first,
                // therefore friend has been added
                ResponseEntity(HttpStatus.OK)

        } catch (e: UserBlockedByGivenUserException) {
            println("blocked")
            ResponseEntity(HttpStatus.CONFLICT)
        } catch (e: FriendRequestAlreadyExistsException) {
            println("request exists")
            ResponseEntity(HttpStatus.CREATED)
        } catch (e: UserNotFoundException) {
            println("not found")
            ResponseEntity(HttpStatus.BAD_REQUEST)
        } catch (e: AlreadyFriendsException) {
            println("already friends")
            ResponseEntity(HttpStatus.OK)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
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

    @PutMapping("/friendRequest/accept/{friendRequestId}")
    fun addFriend(
        @PathVariable("friendRequestId") friendRequestId: String
    ): ResponseEntity<Any> {

        return try {
            userService.addFriend(friendRequestId)
            ResponseEntity(HttpStatus.OK)
        } catch(e: UserNotFoundException) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        } catch(e: UserBlockedByGivenUserException) {
            ResponseEntity(HttpStatus.CONFLICT)
        } catch (e: Exception) {
            println(e.printStackTrace())
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }

    @PostMapping("/friendRequest/reject/{friendRequestId}")
    fun rejectFriendRequest(
        @PathVariable("friendRequestId") friendRequestId: String
    ): ResponseEntity<Any> {
        return try {
            friendRequestService.rejectFriendRequest(friendRequestId)
            ResponseEntity(HttpStatus.OK)
        } catch(e: UserNotFoundException) {
            ResponseEntity(HttpStatus.OK)
        } catch (e: Exception) {
            println(e.printStackTrace())
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/change/nickname/{userId}/{nickname}")
    fun changeNickname(
        @PathVariable("userId") userId: String,
        @PathVariable("nickname") nickname: String,
    ): ResponseEntity<Boolean> {
        TODO("Not yet implemented")
    }

}