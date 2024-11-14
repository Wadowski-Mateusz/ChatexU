package com.example.server.controller

import com.example.server.commons.default
import com.example.server.converters.FriendRequestMapper
import com.example.server.converters.UserMapper
import com.example.server.dto.FriendDto
import com.example.server.dto.FriendRequestDto
import com.example.server.dto.UserDto
import com.example.server.dto.UserViewDto
import com.example.server.exceptions.AlreadyFriendsException
import com.example.server.exceptions.FriendRequestAlreadyExistsException
import com.example.server.exceptions.UserBlockedByGivenUserException
import com.example.server.exceptions.UserNotFoundException
import com.example.server.model.User
import com.example.server.service.ChatService
import com.example.server.service.FriendRequestService
import com.example.server.service.UserService
import lombok.AllArgsConstructor
import org.bson.types.ObjectId
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
    private val chatService: ChatService,
) {

    @GetMapping("/get/{userId}")
    fun getUser(@PathVariable("userId") userId: String): ResponseEntity<UserDto> {
        return try {
            val user = userService.getUserById(userId)
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

    @GetMapping("/nickname_part/{searcherId}/{partOfNickname}")
    fun getUserByPartOfNickname(
        @PathVariable("searcherId") searcherId: String,
        @PathVariable("partOfNickname") partOfNickname: String
    ): ResponseEntity<List<UserViewDto>> {

        return try {
            val users: List<User> = userService.getUsersByPartOfNickname(partOfNickname)
            val notBlockingUsers =
                users.filterNot { foundUser ->
                    userService.isUserBlockedByGivenUser(foundUser.userId.toString(), searcherId)
                }
            val notBlockingUsersDtos: List<UserViewDto> = notBlockingUsers.map { UserMapper.toViewDto(it) }
            println("getUserByPartOfNickname size: ${notBlockingUsersDtos.size}")
            ResponseEntity(notBlockingUsersDtos, HttpStatus.OK)
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
//            val requests = friendRequestService.findAllRequestsForUserAsRecipient(userId)
//                .map { FriendRequestMapper.toDto(it) }
            val requests = friendRequestService.findAllRequestsForUser(userId)
                .map { FriendRequestMapper.toDto(it) }
            println("UserController::getAllFriendRequestsForUser() - size: ${requests.size}")
            ResponseEntity(requests, HttpStatus.OK)
        } catch (e: UserNotFoundException) {
            e.printStackTrace()
            ResponseEntity(HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }


    @GetMapping("/friends/{userId}")
    fun getUserFriends(
        @PathVariable("userId") userId: String
    ): ResponseEntity<List<FriendDto>> {
        println("getUserFriends(): $userId")

        return try {
            val friends: List<User> = userService.getUserFriends(userId)
            val friendDtos = friends.map { UserMapper.toFriendDto(userId, it, chatService) }
            ResponseEntity(friendDtos, HttpStatus.OK)
        } catch (e: UserNotFoundException) {
            e.printStackTrace()
            ResponseEntity(HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


//    @GetMapping("/friends/{userId}/{phrase}")
//    fun getUserFriendsByNickname(
//        @PathVariable("userId") userId: String,
//        @PathVariable("phrase") phrase: String
//    ): ResponseEntity<List<FriendDto>> {
//        println("getUserFriendsByNickname() + $userId")
//        return try {
//            val friends: List<User> = userService.getUserFriends(userId, phrase)
//            val friendDtos = friends.map { UserMapper.toFriendDto(userId, it, chatService) }
//            println("Returning list of friends of size: ${friendDtos.size}")
//            ResponseEntity(friendDtos, HttpStatus.OK)
//        } catch (e: UserNotFoundException) {
//            e.printStackTrace()
//            ResponseEntity(HttpStatus.BAD_REQUEST)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
//        }
//    }




    @PostMapping("/friend/send_request/{senderId}/{recipientId}")
    fun sendFriendRequest(
        @PathVariable("senderId") senderId: String,
        @PathVariable("recipientId") recipientId: String,
    ): ResponseEntity<Any> {
        return try {
            if(userService.checkIfFriends(senderId, recipientId))
                throw AlreadyFriendsException()
            val request = userService.createFriendRequest(senderId, recipientId)
            if (request.requestId.toString() != ObjectId().default().toString()) {
                // request created
                val requestDto = FriendRequestMapper.toDto(request)
                println("friend request success - id: ${request.requestId}")
                ResponseEntity(requestDto, HttpStatus.CREATED)
            }
            else {
                // request not created,
                // because recipient has already sent request first,
                // therefore friend has been added
                println("already recived request from that user, creating friends")
                ResponseEntity(HttpStatus.OK)
            }

        } catch (e: UserBlockedByGivenUserException) {
            println("friend request - blocked user")
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

    // TODO same as rejectFriendRequest()?
    @DeleteMapping("/friendRequest/delete/{requestId}")
    fun deleteRequest(
        @PathVariable("requestId") requestId: String
    ): ResponseEntity<Any> {
        println("/friendRequest/delete/{requestId}: request to delete $requestId")
        return try {
            friendRequestService.deleteRequest(requestId)
            println("friendRequest/delete/{requestId}: = success http.OK")
            ResponseEntity(HttpStatus.OK)
        } catch(e: UserNotFoundException) {
            println("/friendRequest/delete/{requestId}: UserNotFoundException()")
            ResponseEntity(HttpStatus.OK)
        } catch (e: Exception) {
            println(e.printStackTrace())
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/block_user/{blockingUserId}/{blockedUserId}")
    fun blockUser(
        @PathVariable("blockingUserId") blockingUserId: String,
        @PathVariable("blockedUserId") blockedUserId: String
    ): ResponseEntity<Any> {
        return try {
            val blockingUser = userService.getUserById(blockedUserId)
            userService.getUserById(blockedUserId) // just to check if user exists
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

    // TODO same as deleteRequest()?
    @PostMapping("/friendRequest/reject/{friendRequestId}")
    fun rejectFriendRequest(
        @PathVariable("friendRequestId") friendRequestId: String
    ): ResponseEntity<Any> {
        return try {
            friendRequestService.deleteRequest(friendRequestId)
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