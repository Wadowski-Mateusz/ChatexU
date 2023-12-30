package com.example.server.converters

import com.example.server.dto.FriendDto
import com.example.server.dto.UserViewDto
import com.example.server.exceptions.ChatNotFoundException
import com.example.server.model.User
import com.example.server.service.ChatService
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource

object UserMapper {

    fun toViewDto(user: User): UserViewDto {
        val userId = user.userId.toString()
        val icon = getProfileIcon(userId)
        return UserViewDto(userId = userId, nickname = user.nickname, icon = icon)
    }
    
    fun toFriendDto(userId: String, friend: User, chatService: ChatService): FriendDto {
        val chatNickname: String =
            try {
                chatService.getChatWithParticipants(listOf(userId, friend.userId.toString()))
                println("TODO - toFriendDto - nicknames")
                "TODO - nicknames"
            } catch (e: ChatNotFoundException) {
                ""
            }
        return FriendDto(
            id = friend.userId.toString(),
            nickname = friend.nickname,
            nicknameFromChat = chatNickname,
            icon = getProfileIcon(friend.userId.toString())
        )
    }

    private fun getProfileIcon(userId: String): ByteArray {
        val resource: Resource = ClassPathResource("icons/${listOf("red","green","blue").random()}.png")
        return resource.inputStream.readAllBytes()
    }
    

}