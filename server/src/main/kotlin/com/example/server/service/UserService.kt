package com.example.server.service

import com.example.server.commons.Constants
import com.example.server.dto.LoginDto
import com.example.server.dto.RegisterDto
import com.example.server.dto.UserDto
import com.example.server.enums.RegisterVerificationPoints
import com.example.server.exceptions.BadLoginDataException
import com.example.server.exceptions.DataAlreadyInTheDatabaseException
import com.example.server.exceptions.ErrorMessageCommons
import com.example.server.exceptions.UserNotFoundException
import com.example.server.model.User
import com.example.server.repository.UserRepository
import lombok.AllArgsConstructor
import org.bson.types.ObjectId
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.pow

@Service
@AllArgsConstructor
class UserService(private val userRepository: UserRepository) {

    fun getUserIconURI(userId: String): String {
        // TODO
        return Constants.DEFAULT_PROFILE_URI
    }

    fun save(user: User): User {
        return userRepository.insert(user)
    }

    fun getById(id: ObjectId): User {
        return userRepository.findById(id.toHexString()).orElseThrow {
                throw UserNotFoundException(ErrorMessageCommons.idNotFound(type = "user", id = id.toHexString()))
            }
    }

    fun getById(id: String): User {
        return userRepository.findById(id).orElseThrow {
                throw UserNotFoundException(ErrorMessageCommons.idNotFound(type = "user", id = id))
            }
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




}
