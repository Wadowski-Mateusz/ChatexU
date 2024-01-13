package com.example.chatexu.converters

import com.example.chatexu.data.remote.dto.FriendRequestDto
import com.example.chatexu.domain.model.FriendRequest
import java.time.Instant

object FriendRequestMapper {

    fun toDto(friendRequest: FriendRequest): FriendRequestDto {
        return FriendRequestDto(
            requestId = friendRequest.requestId,
            senderId = friendRequest.senderId,
            recipientId = friendRequest.recipientId,
            created = friendRequest.created.toString()
        )
    }

    fun fromDto(friendRequestDto: FriendRequestDto): FriendRequest {
        return FriendRequest(
            requestId = friendRequestDto.requestId,
            senderId = friendRequestDto.senderId,
            recipientId = friendRequestDto.recipientId,
            created = Instant.parse(friendRequestDto.created)
        )
    }
}