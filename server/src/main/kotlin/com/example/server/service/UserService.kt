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
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import lombok.AllArgsConstructor
import org.bson.types.ObjectId
import org.jetbrains.annotations.TestOnly
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.security.Key
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrNull
import kotlin.math.log
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
): UserDetailsService {
    @Lazy
    @Autowired
    private val _friendRequestService: FriendRequestService? = null
    private val friendRequestService: FriendRequestService by lazy { _friendRequestService!! }
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    private val KEY =
        "70337336763979244226452948404D635166546A576E5A7134743777217A25432A462D4A614E645267556B58703273357538782F413F4428472B4B6250655368"
    private val KEY_LIFESPAN = 1000L * 60L * 60L * 24L * 30L


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
    fun saveAll(users: List<User>) {
        logger.info("UserService.saveAll()")

        // TODO bad fix for token saving problems
        val usersToSave = users.map { it.copy(tokens = emptyMap()) }
        userRepository.saveAll(usersToSave)
        usersToSave.forEach {newUser ->
            saveToken(
                newUser,
                users.first { it.userId == newUser.userId }.tokens.keys.first()
                )
        }

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
//        val username: String = registerDto.username
        val password: String = registerDto.password

        require(email.isNotBlank()) { ErrorMessageCommons.isBlank(Field.EMAIL, "UserService.verifyRegisterDto()") }
        require(nickname.isNotBlank()) { ErrorMessageCommons.isBlank(Field.NICKNAME, "UserService.verifyRegisterDto()") }
//        require(username.isNotBlank()) { ErrorMessageCommons.isBlank(Field.USERNAME, "UserService.verifyRegisterDto()") }
        require(password.isNotBlank()) { ErrorMessageCommons.isBlank(Field.PASSWORD, "UserService.verifyRegisterDto()") }


        // flags sum
        var inUse: Int = 0

        if(userRepository.existsByEmail(email))
            inUse += (2.0).pow(RegisterVerificationPoints.EMAIL.ordinal).toInt()
        if(userRepository.existsByNickname(nickname))
            inUse += (2.0).pow(RegisterVerificationPoints.NICKNAME.ordinal).toInt()
//        if(userRepository.existsByUsername(username))
//            inUse += (2.0).pow(RegisterVerificationPoints.USERNAME.ordinal).toInt()

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
            username = registerDto.nickname,
            email = registerDto.email,
            password = registerDto.password,
            profilePictureUri = Constants.DEFAULT_PROFILE_URI,
            friends = setOf(),
            blockedUsers = setOf(),
            tokens = emptyMap(),
            role = Constants.ROLE_USER
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
        println("login -3.1")
        require(login.isNotBlank()) { ErrorMessageCommons.isBlank(Field.LOGIN, "UserService.login()") }
        require(password.isNotBlank()) { ErrorMessageCommons.isBlank(Field.PASSWORD, "UserService.login()") }
        println("login -3.2")
        return if(login.contains('@')) {
            println("login -3.3")
            println(login)
            println(password)
            userRepository.findByEmailAndPassword(login, password)
        } else {
            println("login -3.4")
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

        val icon = user.getIconAsByteArray()

        return UserDto(
            userId = user.userId.toHexString(),
            nickname = user.nickname,
            username = user.username,
            email = user.email,
            password = user.password,
            profilePictureUri = user.profilePictureUri,
            friends = user.friends,
            blockedUsers = user.blockedUsers,
            profilePicture = icon,
            role = user.role
        )
    }

    /**
     * Convert user to dto
     *
     * @param user
     * @return
     */
    @Throws(IOException::class)
    fun convertToDto(userId: String): UserDto {

        require( ObjectId.isValid(userId) ) {
            ErrorMessageCommons.objectIdIsNotValid(
                objectIdValue = userId,
                className = ClassName.USER,
                functionName = "UserService.convertToDto()"
            )
        }

        val user: User = getUserById(userId)
        return convertToDto(user)
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
            acceptFriendRequest(existingRequest.requestId.toString())
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
    fun acceptFriendRequest(friendRequestId: String) {

        logger.info("UserService.acceptFriendRequest()")

        require( ObjectId.isValid(friendRequestId) ) {
            ErrorMessageCommons.objectIdIsNotValid(
                objectIdValue = friendRequestId,
                className = ClassName.FRIEND_REQUEST,
                functionName = "UserService.acceptFriendRequest()"
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

    /**
     * Delete friendship
     *
     * @param userId
     * @param friendId
     * @return
     */
    fun deleteFriend(userId: String, friendId: String): Boolean {
        logger.info("UserService.deleteFriend()")

        require( ObjectId.isValid(userId) ) {
            ErrorMessageCommons.objectIdIsNotValid(
                objectIdValue = userId,
                className = ClassName.USER,
                functionName = "UserService.deleteFriend()"
            )
        }

        require( ObjectId.isValid(friendId) ) {
            ErrorMessageCommons.objectIdIsNotValid(
                objectIdValue = userId,
                className = ClassName.USER,
                functionName = "UserService.deleteFriend()"
            )
        }

        val del1 = userRepository.deleteFriendsByUserId(userId, friendId)
        val del2 = userRepository.deleteFriendsByUserId(friendId, userId)

        return del1 + del2 > 1

    }

    fun updateUserNickname(userId: String, nickname: String): User {
        logger.info("UserService.updateUserNickname()")

        require( ObjectId.isValid(userId) ) {
            ErrorMessageCommons.objectIdIsNotValid(
                objectIdValue = userId,
                className = ClassName.USER,
                functionName = "UserService.deleteFriend()"
            )
        }

        if(nickname.isBlank())
            throw Exception("New nickname is blank")

        val countUpdatedDocuments: Long = userRepository.updateNicknameByUserId(userId, nickname)
        if (countUpdatedDocuments < 1) {
            val u = getUserById(userId)
            // if getUserById is not throwing an error,
            // that means, user is in the database, but has the same nickname as new one
            return u
        }

        return getUserById(userId)
    }

    // TODO hardcoded icons path
    // TODO No validation on delete
    @Throws(Exception::class)
    fun updateUserIcon(userId: String, icon: MultipartFile): User {

        val resourceFolder: File = File("src/main/resources/icons")

        if (!resourceFolder.exists())
            throw Exception("Icon catalog not found! Icon directory path: $resourceFolder")


        val user: User = getUserById(userId)

        val iconAsBytes: ByteArray = icon.bytes
        val iconUriName: String = ObjectId().toHexString() + ".png"
        val outputFile: File = File(resourceFolder, iconUriName)

        FileOutputStream(outputFile).use { outputStream -> outputStream.write(iconAsBytes) }

        val result = userRepository.updateProfilePictureUriByUserId(userId, iconUriName)

        val oldIconName = user.profilePictureUri

        if (result > 0) {

            val defaultsIcons = arrayOf<String>(
                "icons/blue.png",
                "icons/default.bmp",
                "icons/error.png",
                "icons/green.png",
                "icons/red.png"
            )
                // TODO files are not deletingo
            if(oldIconName !in defaultsIcons) {
                val fileToDelete = File(resourceFolder, oldIconName)
//                println("delete ${fileToDelete.absoluteFile}")
                if(fileToDelete.delete()){}
//                    println("t")
                else {}
//                    println("f")
            }
        } else {
            throw Exception("Icon update has failed")
        }

        return getUserById(userId)
    }

    fun getUserByLogin(login: String): User {
        return if (login.contains('@')) {
            userRepository.findUserByEmail(login)
                ?: throw UserNotFoundException(ErrorMessageCommons.notFound(
                        ClassName.USER,
                        Field.EMAIL,
                        login,
                        "UserService.getUserByLogin(login)"
                    ))
        }
        else
            userRepository.findByNickname(login)
                ?: throw UserNotFoundException(ErrorMessageCommons.notFound(
                    ClassName.USER,
                    Field.NICKNAME,
                    login,
                    "UserService.getUserByLogin(login)"
                ))
    }

    /************************************ TOKENS ************************************/

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(login: String): UserDetails {
        try {
            return getUserByLogin(login)
        } catch (e: UserNotFoundException) {
            throw BadLoginDataException(ErrorMessageCommons.notFound(
                ClassName.USER,
                Field.LOGIN,
                login,
                "UserService.loadUserByUsername(login)"
            ))
        }
    }

    fun saveToken(user: User, jwt: String): Boolean {
        return saveToken(user.userId.toString(), jwt)
//        expireAllUserTokens(user)
//        val success = userRepository.saveNewTokensByUserId(user.userId.toString(), jwt, false)
//        return success > 0
//        return true
    }

    fun saveToken(userId: String, jwt: String): Boolean {
//        expireAllUserTokens(user)
        val success = userRepository.saveNewTokensByUserId(userId, jwt, false)
        return success > 0
//        return true
    }



    fun expireAllUserTokens(user: User) {

        val tokens: Map<String, Boolean> = user.tokens.map { (key, _) -> key to false }.toMap()

        val userWithExpiredTokens: User = user.copy(
            tokens = tokens
        )

        userRepository.saveNewTokensByUserId(user.userId.toString(), tokens.keys.toList(), tokens.values.toList())
    }

//    fun findByToken(jwt: String?): User {
//        return userRepository.findByToken(jwt)
//    }


    @Throws(MalformedJwtException::class)
    fun extractLogin(token: String): String {
        return extractClaim(token, Claims::getSubject) // TODO change to `Claims::subject` in kotlin 2.2 () - https://youtrack.jetbrains.com/issue/KT-8575/Support-Java-synthetic-property-references
    }

    fun isTokenValid(token: String, securityUserDetails: UserDetails): Boolean {
        val login = extractLogin(token)
        return login == securityUserDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration) // TODO change to `Claims::expiration` in kotlin 2.2 - https://youtrack.jetbrains.com/issue/KT-8575/Support-Java-synthetic-property-references
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    fun createToken(user: User): String {
        val map = HashMap<String, Any?>()
        map["userId"] = user.userId
        map["role"] = user.role
        map["email"] = user.email
        map["username"] = user.email

        return createToken(map, user)
    }

    private fun createToken(
        extraClaims: Map<String, Any?>?,
        securityUserDetails: UserDetails
    ): String {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(securityUserDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + KEY_LIFESPAN))
            .signWith(getSignInKey(), SignatureAlgorithm.HS512)
            .compact()
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSignInKey(): Key {
        val keyBytes = Decoders.BASE64.decode(KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }


}
