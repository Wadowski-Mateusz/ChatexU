package com.example.server.converters

import com.example.server.dto.FriendDto
import com.example.server.dto.UserViewDto
import com.example.server.exceptions.ChatNotFoundException
import com.example.server.model.User
import com.example.server.service.ChatService

object UserMapper {

    fun toViewDto(user: User): UserViewDto {
        val userId = user.userId.toString()
        val icon = user.getIconAsByteArray()
        return UserViewDto(userId = userId, nickname = user.nickname, username = user.username, icon = icon)
    }
    
    fun toFriendDto(userId: String, friend: User, chatService: ChatService): FriendDto {
        val chatNickname: String =
            try {
                chatService.getChatWithParticipants(listOf(userId, friend.userId.toString()))
//                "TODO - chat nicknames"
                ""
            } catch (e: ChatNotFoundException) {
                ""
            }
        return FriendDto(
            id = friend.userId.toString(),
            nickname = friend.nickname,
            nicknameFromChat = chatNickname,
            icon = friend.getIconAsByteArray()
        )
    }
    

}