package com.example.server.dto

import com.example.server.model.MessageContent
import java.time.Instant

data class ChatViewIconDto(
    val chatId: String,
    val chatName: String,
    val lastMessage: String,
    val timestamp: Instant,
    val icon: ByteArray,
    // val isMuted
)