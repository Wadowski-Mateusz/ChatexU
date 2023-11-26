package com.example.server.converters

import com.example.server.dto.UserViewDto
import com.example.server.model.User
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource

object UserMapper {

    fun toViewDto(user: User): UserViewDto {
        val userId = user.userId.toString()
        val icon = getProfileIcon(userId)
        return UserViewDto(userId = userId, nickname = user.nickname, icon = icon)
    }

    private fun getProfileIcon(userId: String): ByteArray {
        val resource: Resource = ClassPathResource("icons/${listOf("red","green","blue").random()}.png")
        return resource.inputStream.readAllBytes()
    }

}