package com.example.server.service

import com.example.server.commons.Constants
import com.example.server.commons.default
import com.example.server.dto.LoginDto
import com.example.server.dto.RegisterDto
import com.example.server.dto.UserDto
import com.example.server.enums.RegisterVerificationPoints
import com.example.server.exceptions.*
import com.example.server.model.FriendRequest
import com.example.server.model.User
import com.example.server.repository.UserRepository
import lombok.AllArgsConstructor
import org.bson.types.ObjectId
import org.jetbrains.annotations.TestOnly
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import kotlin.jvm.optionals.getOrNull
import kotlin.math.pow

@Service
@AllArgsConstructor
class UserService(
    private val userRepository: UserRepository,
) {
    @Lazy
    @Autowired
    private val _friendRequestService: FriendRequestService? = null
    private val friendRequestService: FriendRequestService by lazy { _friendRequestService!! }

    fun getUserIconURI(userId: String): String {
        // TODO
        println("getUserIconURO TODO")
        return Constants.DEFAULT_PROFILE_URI
    }

    fun getUserByNickname(nickname: String): User {
        return userRepository.findByNickname(nickname)
            ?: throw UserNotFoundException()
    }


    @Transactional
    fun save(user: User): User {
        return userRepository.save(user)
    }

    @Transactional
    fun saveAll(users: List<User>): List<User> {
        return userRepository.saveAll(users)
    }

    fun getById(id: ObjectId): User {
        return userRepository.findById(id.toHexString()).getOrNull()
            ?: throw UserNotFoundException(ErrorMessageCommons.idNotFound(type = "user", id = id.toHexString()))
    }

    fun getById(id: String): User {
        return userRepository.findById(id).getOrNull()
            ?: throw UserNotFoundException(ErrorMessageCommons.idNotFound(type = "user", id = id))
    }


    @Transactional
    fun createUserFromRegisterDto(registerDto: RegisterDto): User {

        val flagsInUse = verifyRegisterDto(registerDto)
        if (flagsInUse > 0)
            throw DataAlreadyInTheDatabaseException(flagsInUse.toString())


        val newUser = User(
            nickname = registerDto.nickname,
            email = registerDto.email,
            password = registerDto.password,
            profilePictureUri = Constants.DEFAULT_PROFILE_URI,
            friends = setOf(),
            blockedUsers = setOf(),
        )

        return save(newUser)

    }

    fun login(loginDto: LoginDto): User {
        val user = userRepository.findByEmailAndPassword(loginDto.email, loginDto.password)
            ?: throw BadLoginDataException()
        return user
    }


    @TestOnly
    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    fun convertToDto(user: User): UserDto {

        val resource: Resource = ClassPathResource("icons/${listOf("red","green","blue").random()}.png")
        val inp = resource.inputStream.readAllBytes()


        return UserDto(
            userId = user.userId.toHexString(),
            nickname = user.nickname,
            email = user.email,
            password = user.password,
            profilePictureUri = user.profilePictureUri,
            friends = user.friends,
            blockedUsers = user.blockedUsers,
            profilePicture = inp
        )
    }


    // Return sum of flags of already used data
    private fun verifyRegisterDto(registerDto: RegisterDto): Int {
        // flags sum
        var inUse = 0

        if(userRepository.existsByEmail(registerDto.email))
            inUse += 2.0.pow(RegisterVerificationPoints.EMAIL.ordinal.toDouble()).toInt()
        if(userRepository.existsByNickname(registerDto.nickname))
            inUse += 2.0.pow(RegisterVerificationPoints.NICKNAME.ordinal.toDouble()).toInt()

        return inUse
    }


    fun checkIfFriends(friend1Id: String, friend2Id: String): Boolean {
        val friends = getById(friend1Id).friends
        return friends.contains(friend2Id)
    }
    @Transactional
    fun createFriendRequest(senderId: String, recipientId: String): FriendRequest {

        // if recipient has already sent request, then just make them friends
        val requests = friendRequestService.findAllRequestsForUserAsSender(recipientId)
        val existingRequest: FriendRequest? = requests.find { it.recipientId.toString() == senderId }

        if (existingRequest != null) {
            // no request created
            addFriend(existingRequest.requestId.toString())
            return FriendRequest(requestId = ObjectId().default(), senderId = ObjectId().default(), recipientId = ObjectId().default(), created = Instant.MIN)
        }

        val request = friendRequestService.createWithoutSave(senderId, recipientId)
        val savedRequest = friendRequestService.save(request)
        return savedRequest
    }

    @Transactional
    fun addFriend(friendRequestId: String) {
        val request: FriendRequest = friendRequestService.findById(friendRequestId)

        val friend1Id: String = request.senderId.toString()
        val friend2Id: String = request.recipientId.toString()

        if (isUserBlockedByGivenUser(friend1Id, friend2Id))
            throw UserBlockedByGivenUserException()

        val friend1 = getById(friend1Id)
        val friend2 = getById(friend2Id)
        // toSet() just in case
        val friend1withNewFriend = friend1.copy(friends = friend1.friends.plus(friend2Id).toSet())
        val friend2withNewFriend = friend2.copy(friends = friend2.friends.plus(friend1Id).toSet())
        saveAll(listOf(friend1withNewFriend, friend2withNewFriend))

        friendRequestService.delete(request)
    }

    fun isUserBlockedByGivenUser(userId: String, userToCheckIfBlockedId: String): Boolean {
        val user = userRepository.findById(userId).getOrNull()
            ?: throw UserNotFoundException()
        return user.blockedUsers.contains(userToCheckIfBlockedId)
    }

    fun getAllFriendRequestsForUser(userId: String): List<FriendRequest> {
        return friendRequestService.findAllRequestsForUserAsRecipient(userId)
            .toList()
    }

    fun getUserFriends(userId: String, partOfNickname: String = ""): List<User> {
        val user = getById(userId)

        var friends = user.friends.asSequence()
            .map { friendId -> getById(friendId) }
            .toList()
        if(partOfNickname.isNotBlank()) {
            println("Not blank: $partOfNickname")
            friends = friends.filter { it.nickname.contains(partOfNickname) }
        }
        return friends
    }

    fun getUsersByPartOfNickname(partOfNickname: String): List<User> {
        val users = userRepository.findAllByNicknameLike(partOfNickname)
        println(users.size)
        return users
    }

}
