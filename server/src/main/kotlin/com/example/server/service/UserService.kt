package com.example.server.service

import com.example.server.commons.Constants
import com.example.server.dto.ChatViewIconDto
import com.example.server.dto.UserDto
import com.example.server.exceptions.ErrorMessageCommons
import com.example.server.exceptions.UserNotFoundException
import com.example.server.model.User
import com.example.server.repository.UserRepository
import lombok.AllArgsConstructor
import org.bson.types.ObjectId
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.time.Instant

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

    fun findUserById(userId: String): User {
        return userRepository.findByUserId(ObjectId(userId))
            ?: throw UserNotFoundException(ErrorMessageCommons.idNotFound(type = "user", id = userId))
    }

    fun findUserById(userId: ObjectId): User {
        return userRepository.findByUserId(userId)
            ?: throw UserNotFoundException(ErrorMessageCommons.idNotFound(type = "user", id = userId.toHexString()))
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


}
