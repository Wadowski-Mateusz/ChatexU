package com.example.server.dto

data class FriendRequestDto(
    val requestId: String,
    val senderId: String,
    val recipientId: String,
    val created: String
)