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
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.time.Instant
import kotlin.Throws
import kotlin.jvm.optionals.getOrNull
import kotlin.math.pow

/**
 * User service
 *
 * @property userRepository
 * @constructor Create User service
 */
@Service
@AllArgsConstructor
class UserService(
    private val userRepository: UserRepository,
) {
    @Lazy
    @Autowired
    private val _friendRequestService: FriendRequestService? = null
    private val friendRequestService: FriendRequestService by lazy { _friendRequestService!! }
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    /**
     * Get user icon URI
     *
     * @param userId of the user which icon URI should be fetched
     * @return the user icon URI; if user has no icon, then default URI is returned
     */
    @Throws(IllegalArgumentException::class)
    fun getUserIconURI(userId: String): String {
        // TODO
        logger.info("UserService.getUserIconURI()")
        logger.warn("UserService.getUserIconURI() returns only default URI.")

        require(userId.isNotBlank()) { ErrorMessageCommons.isBlank(Field.USER_ID, "UserService.getUserIconURI()") }
        return Constants.DEFAULT_PROFILE_URI
    }

    /**
     * Get user by nickname
     *
     * @param nickname of the user which should be fetched
     * @return the user with given nickname
     */
    @Throws(IllegalArgumentException::class, UserNotFoundException::class)
    fun getUserByNickname(nickname: String): User {
        logger.info("UserService.getUserByNickname()")

        require(nickname.isNotBlank()) { ErrorMessageCommons.isBlank(Field.NICKNAME, "UserService.getUserByNickname()") }
        return userRepository.findByNickname(nickname)
            ?: throw UserNotFoundException(ErrorMessageCommons.notFound(
                className = ClassName.USER,
                field = Field.NICKNAME,
                value = nickname,
                functionName = "UserService.getUserByNickname()"
            ))
    }

    /**
     * Get user by username
     *
     * @param username of the user which should be fetched
     * @return the user with given username
     */
    @Throws(IllegalArgumentException::class, UserNotFoundException::class)
    fun getUserByUsername(username: String): User {
        logger.info("UserService.getUserByUsername()")

        require(username.isNotBlank()) { ErrorMessageCommons.isBlank(Field.NICKNAME, "UserService.getUserByUsername()") }

        return userRepository.findByUsername(username)
            ?: throw UserNotFoundException(ErrorMessageCommons.notFound(
                className = ClassName.USER,
                field = Field.USERNAME,
                value = username,
                functionName = "UserService.getUserByUsername()"
            ))
    }

    /**
     * Get user by email
     *
     * @param email
     * @return
     */
    @Throws(IllegalArgumentException::class, UserNotFoundException::class)
    fun getUserByEmail(email: String): User {
        logger.info("UserService.getUserByEmail()")

        require(email.isNotBlank()) { "Provided email is blank - UserService.getUserByEmail()" }
        return userRepository.findUserByEmail(email)
            ?: throw UserNotFoundException(
                ErrorMessageCommons.notFound(
                    className = ClassName.USER,
                    field = Field.EMAIL,
                    value = email,
                    functionName = "UserService.getUserByEmail()"
                )
            )
    }

    /**
     * Save user in the database
     *
     * @param user the user to save
     * @return saved user
     */
    @Transactional
    fun save(user: User): User {
        logger.info("UserService.save()")
        return userRepository.save(user)
    }

    /**
     * Save all given users in the database. Test only.
     *
     * @param users
     * @return
     */
    @Transactional
    fun saveAll(users: List<User>): List<User> {
        logger.info("UserService.saveAll()")
        return userRepository.saveAll(users)
    }

    /**
     * Get user by id
     *
     * @param userId
     * @return
     */
    @Throws(IllegalArgumentException::class, UserNotFoundException::class)
    fun getUserById(userId: ObjectId): User {
        logger.info("UserService.getUserById(ObjectId)")
        require( ObjectId.isValid(userId.toHexString()) ) { ErrorMessageCommons.objectIdIsNotValid(
            objectIdValue = userId.toHexString(),
            className = ClassName.USER,
            functionName = "UserService.getUserById(ObjectId)"
        ) }


        return userRepository.findById(userId.toHexString()).getOrNull()
            ?: throw UserNotFoundException(ErrorMessageCommons.notFound(
                ClassName.USER,
                Field.ID,
                userId.toHexString(),
                "UserService.getUserById(ObjectId)"
            ))
    }

    /**
     * Get user by id
     *
     * @param userId
     * @return
     */
    @Throws(IllegalArgumentException::class, UserNotFoundException::class)
    fun getUserById(userId: String): User {
        logger.info("UserService.getUserById(String)")

        require( ObjectId.isValid(userId) ) { ErrorMessageCommons.objectIdIsNotValid(
            objectIdValue = userId,
            className = ClassName.USER,
            functionName = "UserService.getUserById(String)"
        ) }

        return userRepository.findById(userId).getOrNull()
            ?: throw UserNotFoundException(ErrorMessageCommons.notFound(
            ClassName.USER,
            Field.ID,
            userId,
            "UserService.getUserById(String)"
        ))
    }

    /**
     * Verify register dto
     *
     * @param registerDto
     * @return sum of flags of already used data
     */
    @Throws(IllegalArgumentException::class)
    private fun verifyRegisterDto(registerDto: RegisterDto): Int {

        logger.info("UserService.verifyRegisterDto()")

        val email: String = registerDto.email
        val nickname: String = registerDto.nickname
        val username: String = registerDto.username
        val password: String = registerDto.password

        require(email.isNotBlank()) { ErrorMessageCommons.isBlank(Field.EMAIL, "UserService.verifyRegisterDto()") }
        require(nickname.isNotBlank()) { ErrorMessageCommons.isBlank(Field.NICKNAME, "UserService.verifyRegisterDto()") }
        require(username.isNotBlank()) { ErrorMessageCommons.isBlank(Field.USERNAME, "UserService.verifyRegisterDto()") }
        require(password.isNotBlank()) { ErrorMessageCommons.isBlank(Field.PASSWORD, "UserService.verifyRegisterDto()") }


        // flags sum
        var inUse: Int = 0

        if(userRepository.existsByEmail(email))
            inUse += (2.0).pow(RegisterVerificationPoints.EMAIL.ordinal).toInt()
        if(userRepository.existsByNickname(nickname))
            inUse += (2.0).pow(RegisterVerificationPoints.NICKNAME.ordinal).toInt()
        if(userRepository.existsByUsername(username))
            inUse += (2.0).pow(RegisterVerificationPoints.USERNAME.ordinal).toInt()

        return inUse
    }


    /**
     * Create user from register dto
     *
     * @param registerDto
     * @return
     */
    @Transactional
    @Throws(IllegalArgumentException::class, DataAlreadyInTheDatabaseException::class)
    fun createUserFromRegisterDto(registerDto: RegisterDto): User {

        val flagsInUse: Int = verifyRegisterDto(registerDto)
        if (flagsInUse > 0)
            throw DataAlreadyInTheDatabaseException(flagsInUse.toString())

        val newUser = User(
            nickname = registerDto.nickname,
            username = registerDto.username,
            email = registerDto.email,
            password = registerDto.password,
            profilePictureUri = Constants.DEFAULT_PROFILE_URI,
            friends = setOf(),
            blockedUsers = setOf(),
        )

        return save(newUser)

    }

    /**
     * Authenticate user.
     *
     * @param loginDto
     * @return if authentication is successful - user object
     *          else - throws an error
     */
    @Throws(IllegalArgumentException::class, BadLoginDataException::class)
    fun login(loginDto: LoginDto): User {

        logger.info("UserService.login()")

        val login: String = loginDto.login
        val password: String = loginDto.password

        require(login.isNotBlank()) { ErrorMessageCommons.isBlank(Field.LOGIN, "UserService.login()") }
        require(password.isNotBlank()) { ErrorMessageCommons.isBlank(Field.PASSWORD, "UserService.login()") }

        return if(login.contains('@')) {
            userRepository.findByEmailAndPassword(login, password)
        } else {
            userRepository.findByUsernameAndPassword(login, password)
        } ?: throw BadLoginDataException()
    }


    /**
     * Find all
     *
     * @return
     */
    @TestOnly
    fun findAll(): List<User> {
        logger.info("UserService.findAll()")
        return userRepository.findAll()
    }

    /**
     * Convert user to dto
     *
     * @param user
     * @return
     */
    @Throws(IOException::class)
    fun convertToDto(user: User): UserDto {
        logger.info("UserService.convertToDto()")
        logger.warn("UserService.convertToDto() uses random icons, not user specific icon")

        val resource: Resource = ClassPathResource("icons/${listOf("red", "green", "blue").random()}.png")

        val inp = resource.inputStream.readAllBytes()

        return UserDto(
            userId = user.userId.toHexString(),
            nickname = user.nickname,
            username = user.username,
            email = user.email,
            password = user.password,
            profilePictureUri = user.profilePictureUri,
            friends = user.friends,
            blockedUsers = user.blockedUsers,
            profilePicture = inp
        )
    }


    /**
     * Check if friends
     *
     * @param user1Id
     * @param user2Id
     * @return if users are friends - true; otherwise false
     */
    @Throws(IllegalArgumentException::class, UserNotFoundException::class)
    fun checkIfFriends(user1Id: String, user2Id: String): Boolean {

        logger.info("UserService.checkIfFriends()")

        require( ObjectId.isValid(user1Id) ) {
            ErrorMessageCommons.objectIdIsNotValid(
                objectIdValue = user1Id,
                className = ClassName.USER,
                functionName = "UserService.checkIfFriends()"
            )
        }

        require( ObjectId.isValid(user2Id) ) {
            ErrorMessageCommons.objectIdIsNotValid(
                objectIdValue = user2Id,
                className = ClassName.USER,
                functionName = "UserService.checkIfFriends()"
            )
        }

        val user1Friends: Set<String> = getUserById(user1Id).friends
        return user1Friends.contains(user2Id)
    }

    /**
     * Create friend request
     *
     * @param senderId
     * @param recipientId
     * @return
     */
    @Transactional
    fun createFriendRequest(senderId: String, recipientId: String): FriendRequest {

        logger.info("UserService.createFriendRequest()")

        require( ObjectId.isValid(senderId) ) {
            ErrorMessageCommons.objectIdIsNotValid(
                objectIdValue = senderId,
                className = ClassName.USER,
                functionName = "UserService.createFriendRequest()"
            )
        }

        require( ObjectId.isValid(recipientId) ) {
            ErrorMessageCommons.objectIdIsNotValid(
                objectIdValue = recipientId,
                className = ClassName.USER,
                functionName = "UserService.createFriendRequest()"
            )
        }

        // if recipient has already sent request, then just make them friends
        val requests = friendRequestService.findAllRequestsForUserAsSender(recipientId)
        val existingRequest: FriendRequest? = requests.find { it.recipientId.toString() == senderId }

        if (existingRequest != null) {
            // no request created
            addFriend(existingRequest.requestId.toString())
            return FriendRequest(
                requestId = ObjectId().default(),
                senderId = ObjectId().default(),
                recipientId = ObjectId().default(),
                created = Instant.MIN
            )
        }

        val request = friendRequestService.createWithoutSave(senderId, recipientId)
        val savedRequest = friendRequestService.save(request)

        return savedRequest
    }

    /**
     * Add friend
     *
     * @param friendRequestId
     */
    @Transactional
    @Throws(
        FriendRequestNotFoundException::class,
        IllegalArgumentException::class,
        UserBlockedByGivenUserException::class
    )
    fun addFriend(friendRequestId: String) {

        logger.info("UserService.addFriend()")

        require( ObjectId.isValid(friendRequestId) ) {
            ErrorMessageCommons.objectIdIsNotValid(
                objectIdValue = friendRequestId,
                className = ClassName.FRIEND_REQUEST,
                functionName = "UserService.addFriend()"
            )
        }

        val request: FriendRequest = friendRequestService.findById(friendRequestId)

        val senderId: String = request.senderId.toString()
        val recipientId: String = request.recipientId.toString()

        if (isUserBlockedByGivenUser(senderId, recipientId))
            throw UserBlockedByGivenUserException()

        val senderUser = getUserById(senderId)
        val recipientUser = getUserById(recipientId)
        // toSet() just in case
        val friend1withNewFriend = senderUser.copy(friends = senderUser.friends.plus(recipientId).toSet())
        val friend2withNewFriend = recipientUser.copy(friends = recipientUser.friends.plus(senderId).toSet())
        saveAll(listOf(friend1withNewFriend, friend2withNewFriend))

        friendRequestService.delete(request)
    }

    /**
     * Is user blocked by given user
     *
     * @param userId
     * @param userIdToCheckIfBlockedId
     * @return
     */
    @Throws(IllegalArgumentException::class, UserNotFoundException::class)
    fun isUserBlockedByGivenUser(userId: String, userIdToCheckIfBlockedId: String): Boolean {

        logger.info("UserService.isUserBlockedByGivenUser()")

        require( ObjectId.isValid(userId) ) {
            ErrorMessageCommons.objectIdIsNotValid(
                objectIdValue = userId,
                className = ClassName.USER,
                functionName = "UserService.isUserBlockedByGivenUser()"
            )
        }

        require( ObjectId.isValid(userIdToCheckIfBlockedId) ) {
            ErrorMessageCommons.objectIdIsNotValid(
                objectIdValue = userIdToCheckIfBlockedId,
                className = ClassName.USER,
                functionName = "UserService.isUserBlockedByGivenUser()"
            )
        }

        val user = userRepository.findById(userId).getOrNull()
            ?: throw UserNotFoundException()
        return user.blockedUsers.contains(userIdToCheckIfBlockedId)
    }

    /**
     * Get all friend requests for user
     *
     * @param userId
     * @return
     */
    @Throws(IllegalArgumentException::class, UserNotFoundException::class)
    fun getAllFriendRequestsForUser(userId: String): List<FriendRequest> {

        logger.info("UserService.getAllFriendRequestsForUser()")

        require( ObjectId.isValid(userId) ) {
            ErrorMessageCommons.objectIdIsNotValid(
                objectIdValue = userId,
                className = ClassName.USER,
                functionName = "UserService.getAllFriendRequestsForUser()"
            )
        }

        // Just to check if user exists
        this.getUserById(userId)

        return friendRequestService.findAllRequestsForUserAsRecipient(userId)
            .toList()
    }

    /**
     * Get user friends
     *
     * @param userId
     * @param partOfNickname
     * @return
     */
    @Throws(IllegalArgumentException::class, UserNotFoundException::class)
    fun getUserFriends(userId: String, partOfNickname: String = ""): List<User> {

        logger.info("UserService.getUserFriends()")

        require( ObjectId.isValid(userId) ) {
            ErrorMessageCommons.objectIdIsNotValid(
                objectIdValue = userId,
                className = ClassName.USER,
                functionName = "UserService.getUserFriends()"
            )
        }

        val user = getUserById(userId)

        var friends = user.friends.asSequence()
            .map { friendId -> getUserById(friendId) }
            .toList()
        if(partOfNickname.isNotBlank()) {
            println("Not blank: $partOfNickname")
            friends = friends.filter { it.nickname.contains(partOfNickname) }
        }
        return friends
    }

    /**
     * Get users by part of nickname
     *
     * @param partOfNickname
     * @return
     */
    fun getUsersByPartOfNickname(partOfNickname: String): List<User> {

        logger.info("UserService.getUsersByPartOfNickname()")

        val users = userRepository.findAllByNicknameLike(partOfNickname)

        logger.info("UserService.getUsersByPartOfNickname() - Users that meet the requirement: ${users.size}")

        return users
    }

}
