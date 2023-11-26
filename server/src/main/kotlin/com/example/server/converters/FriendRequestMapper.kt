package com.example.server.converters

import com.example.server.dto.FriendRequestDto
import com.example.server.model.FriendRequest
import org.bson.types.ObjectId
import java.time.Instant

object FriendRequestMapper {

    fun toDto(friendRequest: FriendRequest): FriendRequestDto {
        return FriendRequestDto(
            requestId = friendRequest.requestId.toString(),
            senderId = friendRequest.senderId.toString(),
            recipientId = friendRequest.recipientId.toString(),
            created = friendRequest.created.toString()
        )
    }

    fun fromDto(friendRequestDto: FriendRequestDto): FriendRequest {
        return FriendRequest(
            requestId = ObjectId(friendRequestDto.requestId),
            senderId = ObjectId(friendRequestDto.senderId),
            recipientId = ObjectId(friendRequestDto.recipientId),
            created = Instant.parse(friendRequestDto.created)
        )
    }
}