package com.example.server.converters


import com.example.server.commons.Constants
import com.example.server.dto.FriendDto
import com.example.server.dto.UserViewDto
import com.example.server.model.User
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import java.io.File
import java.util.UUID

class UserMapperTest {


    @Test
    fun shouldMapUserToViewDto() {
        val user: User = User(
            userId = ObjectId(),
            nickname = "Clay Bradley",
            username = "Lula Lindsey",
            email = "dino.reyes@example.com",
            password = "reprimique",
            profilePictureUri = "default.png",
            friends = setOf(),
            token = "",
            role = Constants.ROLE_USER
        )

        val viewDto: UserViewDto = UserMapper.toViewDto(user)

        assertEquals(viewDto.userId, user.userId.toString())
        assertEquals(viewDto.nickname, user.nickname)
        assertEquals(viewDto.username, user.username)

        val iconPath = user.profilePictureUri.removePrefix("icons/")
        val resourceFolder: File = File("src/main/resources/icons")
        val resource: File = File(resourceFolder, iconPath)
        val bytes: ByteArray  = resource.inputStream().readAllBytes()

        assert(viewDto.icon.contentEquals(bytes))

    }

//    companion object {
//        @JvmStatic
//        @BeforeAll
//        fun beforeAll(): Unit {
//            TODO("12Not yet implemented")
//        }
//
//        @JvmStatic
//        @AfterAll
//        fun afterAll(): Unit {
//            TODO("12Not yet implemented")
//        }
//
//    }
}