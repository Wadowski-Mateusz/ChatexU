package com.example.server.dto

import com.example.server.model.MessageContent
import java.time.Instant

data class ChatViewDto(
    val chatId: String,
    val chatName: String,
    val lastMessageContent: MessageContent,
    val lastMessageSender: String,
    val timestamp: Instant,
    val icon: ByteArray,
    // val isMuted
)