package com.example.server.controller

import com.example.server.commons.default
import com.example.server.converters.FriendRequestMapper
import com.example.server.converters.UserMapper
import com.example.server.dto.FriendDto
import com.example.server.dto.FriendRequestDto
import com.example.server.dto.UserDto
import com.example.server.dto.UserViewDto
import com.example.server.exceptions.*
import com.example.server.model.User
import com.example.server.service.ChatService
import com.example.server.service.FriendRequestService
import com.example.server.service.UserService
//import lombok.AllArgsConstructor
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
    private val friendRequestService: FriendRequestService,
    private val chatService: ChatService,
) {

    private val logger = LoggerFactory.getLogger(UserController::class.java)

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
//            if(userService.isUserBlockedByGivenUser(user.userId.toString(), searcherId))
//                throw UserBlockedByGivenUserException()
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
//            val notBlockingUsers =
//                users.filterNot { foundUser ->
//                    userService.isUserBlockedByGivenUser(foundUser.userId.toString(), searcherId)
//                }
//            val notBlockingUsersDtos: List<UserViewDto> = notBlockingUsers.map { UserMapper.toViewDto(it) }
            val userViewDtos: List<UserViewDto> = users.map { UserMapper.toViewDto(it) }
            ResponseEntity(userViewDtos, HttpStatus.OK)


            //  TODO blocked users implementation
//            val users: List<User> = userService.getUsersByPartOfNickname(partOfNickname)
//            val notBlockingUsers =
//                users.filterNot { foundUser ->
//                    userService.isUserBlockedByGivenUser(foundUser.userId.toString(), searcherId)
//                }
//            val notBlockingUsersDtos: List<UserViewDto> = notBlockingUsers.map { UserMapper.toViewDto(it) }
//            logger.info("UserController.getUserByPartOfNickname() size: ${notBlockingUsersDtos.size}")
//            notBlockingUsersDtos.forEach{logger.info("fetched users: ${ it.nickname }")}
//            ResponseEntity(notBlockingUsersDtos, HttpStatus.OK)
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
    fun checkAreUserFriends(
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
                println("already received request from that user, creating friends")
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

//    @PutMapping("/block_user/{blockingUserId}/{blockedUserId}")
//    fun blockUser(
//        @PathVariable("blockingUserId") blockingUserId: String,
//        @PathVariable("blockedUserId") blockedUserId: String
//    ): ResponseEntity<Any> {
//        return try {
//            val blockingUser = userService.getUserById(blockedUserId)
//            userService.getUserById(blockedUserId) // just to check if user exists
//            val updatedBlockedUsersSet = blockingUser.blockedUsers + blockedUserId
//            userService.save(blockingUser.copy(blockedUsers = updatedBlockedUsersSet))
//
//            ResponseEntity(HttpStatus.OK)
//
//        } catch (e: UserNotFoundException) {
//            ResponseEntity(HttpStatus.NOT_FOUND)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            ResponseEntity( HttpStatus.INTERNAL_SERVER_ERROR)
//        }
//    }

    @PostMapping("/friendRequest/accept/{friendRequestId}")
    fun acceptFriendRequest(
        @PathVariable("friendRequestId") friendRequestId: String
    ): ResponseEntity<Any> {

        return try {
            userService.acceptFriendRequest(friendRequestId)
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
    @DeleteMapping("/friendRequest/reject/{friendRequestId}")
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

    @PutMapping("/update_nickname/{userId}/{nickname}")
    fun changeNickname(
        @PathVariable("userId") userId: String,
        @PathVariable("nickname") nickname: String,
    ): ResponseEntity<UserDto> {
        return try {
            val user: User = userService.updateUserNickname(userId, nickname)
            ResponseEntity(
                userService.convertToDto(user),
                HttpStatus.OK
            )
        } catch(e: UserNotFoundException) {
            println(e.printStackTrace())
            ResponseEntity(HttpStatus.NOT_FOUND)
        } catch (e: DataAlreadyInTheDatabaseException) {
            ResponseEntity(HttpStatus.CONFLICT)
        } catch (e: Exception) {
            println(e.printStackTrace())
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


    @PutMapping("/update_icon/{userId}")
    fun updateIcon(
        @RequestPart("icon") icon: MultipartFile,
        @PathVariable("userId") userId: String,
    ): ResponseEntity<UserDto> {
        return try {
            val user: User = userService.updateUserIcon(userId, icon)
            ResponseEntity(
                userService.convertToDto(user),
                HttpStatus.OK
            )
        } catch(e: UserNotFoundException) {
            println(e.printStackTrace())
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
        catch (e: Exception) {
            println(e.printStackTrace())
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }



//    @Multipart
//    @PUT("${Constants.USER_MAPPING}/update_icon/{userId}")
//    suspend fun putUpdateIcon(
//        @Path("userId") userId: String,
//        @Part icon: MultipartBody.Part
//    ): Response<UserDto>



    @DeleteMapping("/friend/delete/{userId}/{friendId}")
    fun deleteFriend(
        @PathVariable("userId") userId: String,
        @PathVariable("friendId") friendId: String,
    ): ResponseEntity<Boolean> {
        return try {
            ResponseEntity(
                userService.deleteFriend(userId, friendId),
                HttpStatus.OK
            )
        } catch(e: UserNotFoundException) {
            ResponseEntity(HttpStatus.OK)
        } catch (e: Exception) {
            println(e.printStackTrace())
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}