package com.example.chatexu.domain.model

import java.time.Instant

data class FriendRequest(
    val requestId: String,
    val senderId: String,
    val recipientId: String,
    val created: Instant
)