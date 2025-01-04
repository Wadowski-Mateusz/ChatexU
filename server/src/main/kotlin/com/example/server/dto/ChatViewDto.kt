package com.example.server.dto

import com.example.server.model.MessageType
import java.time.Instant

data class ChatViewDto(
    val chatId: String,
    val chatName: String,
    val lastMessageType: MessageType,
    val lastMessageSender: String,
    val timestamp: Instant,
    val icon: ByteArray,
)